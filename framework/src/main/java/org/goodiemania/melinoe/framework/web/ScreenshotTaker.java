package org.goodiemania.melinoe.framework.web;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.goodiemania.melinoe.framework.session.logging.LogFileManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

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

    private TakesScreenshot takesScreenshot;
    private JavascriptExecutor js;
    private WebElement lastElem = null;

    public ScreenshotTaker(final TakesScreenshot takesScreenshot, final JavascriptExecutor javascriptExecutor) {
        this.takesScreenshot = takesScreenshot;
        this.js = javascriptExecutor;
    }


    public File takeScreenshot() {
        File screenshot = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File imageFile = LogFileManager.getInstance().createImageFile();

        try {
            FileUtils.copyFile(screenshot, imageFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return imageFile;
    }

    public File takeScreenshot(final WebElement element) {
        String boarderId = highlightElement(element);
        File file = takeScreenshot();
        unhighlightLast(boarderId);
        return file;
    }

    private String highlightElement(WebElement elem) {
        lastElem = elem;
        return (String) (js.executeScript(SCRIPT_HIGHLIGHT_ELEMENT, elem));
    }

    private void unhighlightLast(final String boarderId) {
        if (lastElem != null) {
            try {
                // if there already is a highlighted element, unhighlight it
                js.executeScript(SCRIPT_UNHIGHLIGHT_ELEMENT, lastElem, boarderId);
            } catch (StaleElementReferenceException ignored) {
                //TODO
                // the page got reloaded, the element isn't there
            } finally {
                // element either restored or wasn't valid, nullify in both cases
                lastElem = null;
            }
        }
    }
}