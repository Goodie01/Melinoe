package nz.geek.goodwin.melinoe.framework.internal.web;

import nz.geek.goodwin.melinoe.framework.api.MelinoeException;
import nz.geek.goodwin.melinoe.framework.internal.log.LogFileManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;

public class ScreenshotTaker {
    private static final String SCRIPT_HIGHLIGHT_ELEMENT = "\n"
            + "var elem = arguments[0]; \n"
            + "if (elem.currentStyle) {\n"
            + "    // Branch for IE 6,7,8. No idea how this works on IE9, but the script\n"
            + "    // should take care of it.\n"
            + "    var style = elem.currentStyle;\n"
            + "    var border = style['borderTopWidth']\n"
            + "            + ' ' + style['borderTopStyle']\n"
            + "            + ' ' + style['borderTopColor']\n"
            + "            + ';' + style['borderRightWidth']\n"
            + "            + ' ' + style['borderRightStyle']\n"
            + "            + ' ' + style['borderRightColor']\n"
            + "            + ';' + style['borderBottomWidth']\n"
            + "            + ' ' + style['borderBottomStyle']\n"
            + "            + ' ' + style['borderBottomColor']\n"
            + "            + ';' + style['borderLeftWidth']\n"
            + "            + ' ' + style['borderLeftStyle']\n"
            + "            + ' ' + style['borderLeftColor'];\n"
            + "} else if (window.getComputedStyle) {\n"
            + "    // Branch for FF, Chrome, Opera\n"
            + "    var style = document.defaultView.getComputedStyle(elem);\n"
            + "    var border = style.getPropertyValue('border-top-width')\n"
            + "            + ' ' + style.getPropertyValue('border-top-style')\n"
            + "            + ' ' + style.getPropertyValue('border-top-color')\n"
            + "            + ';' + style.getPropertyValue('border-right-width')\n"
            + "            + ' ' + style.getPropertyValue('border-right-style')\n"
            + "            + ' ' + style.getPropertyValue('border-right-color')\n"
            + "            + ';' + style.getPropertyValue('border-bottom-width')\n"
            + "            + ' ' + style.getPropertyValue('border-bottom-style')\n"
            + "            + ' ' + style.getPropertyValue('border-bottom-color')\n"
            + "            + ';' + style.getPropertyValue('border-left-width')\n"
            + "            + ' ' + style.getPropertyValue('border-left-style')\n"
            + "            + ' ' + style.getPropertyValue('border-left-color');\n"
            + "}\n"
            + "// highlight the element\n"
            + "elem.style.border = '2px solid red';\n"
            + "return border;";
    private static final String SCRIPT_UNHIGHLIGHT_ELEMENT = "var elem = arguments[0];\n"
            + "var borders = arguments[1].split(';');\n"
            + "elem.style.borderTop = borders[0];\n"
            + "elem.style.borderRight = borders[1];\n"
            + "elem.style.borderBottom = borders[2];\n"
            + "elem.style.borderLeft = borders[3];";

    private final RemoteWebDriver remoteWebDriver;
    private final LogFileManager logFileManager;
    private WebElement lastElem = null;

    public ScreenshotTaker(final RemoteWebDriver remoteWebDriver, LogFileManager logFileManager) {
        this.remoteWebDriver = remoteWebDriver;
        this.logFileManager = logFileManager;
    }

    public String takeScreenshot() {
        File originalImage;
        if(remoteWebDriver instanceof FirefoxDriver ffd) {
            originalImage = ffd.getFullPageScreenshotAs(OutputType.FILE);
        } else {
            originalImage = remoteWebDriver.getScreenshotAs(OutputType.FILE);
        }

        File imageFile = logFileManager.createNewImageFile();

        try {
            FileUtils.copyFile(originalImage, imageFile);

            return logFileManager.getRunDirectory().toPath().relativize(imageFile.toPath()).toString();
        } catch (IOException e) {
            throw new MelinoeException("Unable to write image", e);
        }
    }

    public String takeScreenshot(final WebElement element) {
        String boarderId = highlightElement(element);
        String file = takeScreenshot();
        unhighlightLast(boarderId);
        return file;
    }

    private String highlightElement(WebElement elem) {
        lastElem = elem;
        return (String) (remoteWebDriver.executeScript(SCRIPT_HIGHLIGHT_ELEMENT, elem));
    }

    private void unhighlightLast(final String boarderId) {
        if (lastElem != null) {
            try {
                // if there already is a highlighted element, unhighlight it
                remoteWebDriver.executeScript(SCRIPT_UNHIGHLIGHT_ELEMENT, lastElem, boarderId);
            } catch (StaleElementReferenceException ignored) {
                throw new MelinoeException("We should never get here", ignored);
            } finally {
                // element either restored or wasn't valid, nullify in both cases
                lastElem = null;
            }
        }
    }
}
