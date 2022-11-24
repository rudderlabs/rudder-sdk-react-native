function isStringArray(elements: any) {
    if (!Array.isArray(elements))
        return false;
    return elements.every(element => isString(element));
}

function isString(element: any) {
    return typeof element === 'string'
}

function getStringArray(element: any) {
    if (isStringArray(element))
        return element;
    if (isString(element))
        return [element];
    return null;
}

export { isString, isStringArray, getStringArray };
