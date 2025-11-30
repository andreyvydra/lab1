import ErrorNotify from "./notifications/errorNotify";
import WarningNotify from "./notifications/warningNotify";

export function errorNotifies(xhr) {
    if (xhr.status === 400) {
        new WarningNotify(
            "Ошибка запроса",
            JSON.parse(xhr.responseText).message
        )
    } else if (xhr.status === 500) {
        new ErrorNotify(
            "Ошибка сервера",
            xhr.responseText
        )
    } else if (xhr.status === 404) {
        new ErrorNotify(
            "Страница не найдена",
            xhr.responseText
        )
    } else if (xhr.status === 403) {
        new WarningNotify(
            "Ошибка доступа",
            "Доступ запрещён!"
        )
    } else if (xhr.status === 401) {
        new WarningNotify(
            "Ошибка доступа",
            "Вы не авторизованы в системе!"
        )
    } else {
        new ErrorNotify(
            "Ошибка",
            JSON.parse(xhr.responseText).message
        )
    }
}