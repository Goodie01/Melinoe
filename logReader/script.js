function extractViewQuery() {
    let rawQueryParam = new URLSearchParams(window.location.search).get('q');

    if (rawQueryParam == null || rawQueryParam === "") {
        return []
    } else {
        return rawQueryParam.split('.')
            .map(value => Number(value))
    }
}

function extractDisplayedLogMessages (log, viewQuery) {
    if (viewQuery.length === 0) {
        return log
    } else {
        let displayedMessages = []
        for (let i = 0; i < viewQuery.length; i++) {
            let index = viewQuery[i];
            console.log(index)
            let maybeMessages = log[index].subSessionMessages;
            console.dir(maybeMessages)

            if (maybeMessages !== undefined) {
                displayedMessages = maybeMessages
            }
        }
        return displayedMessages
    }
}

function buildNewSearchQuery(i,viewQuery) {
    if (viewQuery.length === 0) {
        return i
    } else {
        return viewQuery.join(".") + "." + i
    }
}