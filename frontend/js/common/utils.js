export function getCookie (name) {
    var match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    if (match) return match[2];
}

export function setTokenToCookie(token) {
    document.cookie='access_token='+token;
}

const redirectExceptions = [
    { from: 'register.html', to: 'login.html' },
    { from: 'login.html', to: 'login.html' }
];

const servicePages = [
    'location.html',
    'person.html',
    'dragon.html',
    'dragon_head.html',
    'dragon_cave.html',
    'coordinates.html'
]

export function redirect(path) {
    const currentUrl = document.location.href;

    for (const exception of redirectExceptions) {
        if (currentUrl.includes(exception.from) && path.includes(exception.to)) {
            return;
        }
    }

    const absolutePath = new URL(path, document.location.origin).href;
    if (absolutePath !== document.location.href) document.location.href = absolutePath;
}

export function getAuthHeader() {
    return { 'Authorization': 'Bearer ' + getCookie("access_token") }
}

export function redirectIfAuthenticated() {
    let token = getCookie("access_token");
    const currentUrl = document.location.href;
    if (token !== undefined && token.length && !servicePages.some(x => currentUrl.includes(x)) ) {
        redirect("dragon.html")
    } else if ((token === undefined || !token.length) && servicePages.some(x => currentUrl.includes(x))) {
        redirect("login.html")
    }
}

export function decodeJWT(token) {
    try {
        // Разделяем токен на части
        const [headerBase64, payloadBase64, signature] = token.split('.');

        // Декодируем Base64 части Header и Payload
        const header = JSON.parse(atob(headerBase64));
        const payload = JSON.parse(atob(payloadBase64));

        return { header, payload, signature };
    } catch (error) {
        console.error('Ошибка при декодировании токена:', error);
        return null;
    }
}