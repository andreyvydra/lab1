export function getCookie (name) {
    var match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    if (match) return match[2];
}

export function setTokenToCookie(token) {
    document.cookie='access_token='+token;
}

export function redirect(path) {
    document.location.href = path;
}

export function getAuthHeader() {
    return { 'Authorization': 'Bearer ' + getCookie("access_token") }
}