import $ from "jquery";
import * as c from "../common/constants";
import {getAuthHeader, redirectIfAuthenticated} from "../common/utils";
import ErrorNotify from "../common/notifications/errorNotify";
import '../../css/scrolls.css'

let formId = '';

export function setValues(id) {
    const formWidget = $(`#${formId}`);

    $.ajax({
        url: `${c.baseUrl}${c.apiUrl}/dragonHead/${id}`,
        type: "GET",
        headers: getAuthHeader(),
        success: (response) => {

            formWidget.find('input[name=is-changeable-input]').prop('checked', response.isChangeable);
            formWidget.find('#size-input').val(response.size);
            formWidget.find('#eyes-count-input').val(response.eyesCount);

            formWidget.find('.submit-button').data('id', id);
        },
        error: (xhr) => {
            if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
            new ErrorNotify('Ошибка при загрузке данных головы дракона', xhr.responseText);
        },
    });
}

export function form(form) {
    formId = form;
}