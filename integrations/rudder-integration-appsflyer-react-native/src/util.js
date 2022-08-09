function isStringArray(elements) {
    if (!Array.isArray(elements))
        return false;
    return elements.every(element => isString(element));
}

function isString(element) {
    return typeof element === 'string'
}

function getStringArray(element) {
    if (isStringArray(element))
        return element;
    if (isString(element))
        return [element];
    return null;
}

export { isString, isStringArray, getStringArray };