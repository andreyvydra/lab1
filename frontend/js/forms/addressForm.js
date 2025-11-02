import $ from "jquery";
import * as c from "../common/constants";
import {getAuthHeader, redirectIfAuthenticated} from "../common/utils";
import ErrorNotify from "../common/notifications/errorNotify";
import '../../css/scrolls.css'

let formId = '';
let currentTownPage = 0, isTownLoading = false, hasMoreTown = true;

export function setValues(id) {
    const formWidget = $(`#${formId}`);
    $.ajax({
        url: `${c.baseUrl}${c.apiUrl}/addresses/${id}`,
        type: "GET",
        headers: getAuthHeader(),
        success: (response) => {
            formWidget.find('input[name=is-changeable-input]').prop('checked', response.isChangeable);
            formWidget.find('#street-input').val(response.street ?? '');
            formWidget.find('#town-input').val(response.town ?? '');

            formWidget.find('.submit-button').data('id', id);
            updateSelectedItems(formWidget, response);
            attachInfiniteScroll(formWidget, response);
        },
        error: (xhr) => {
            if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
            new ErrorNotify('Ошибка при загрузке адреса', xhr.responseText);
        },
    });
}

function attachInfiniteScroll(formWidget, response) {
    formWidget.find('#town-list-container').off('scroll').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isTownLoading && hasMoreTown) {
            loadTowns(currentTownPage + 1);
            updateSelectedItems(formWidget, response);
        }
    });
}

function updateSelectedItems(formWidget, response) {
    formWidget.find('#town-list div').removeClass('selected');
    formWidget.find(`#town-list div[data-id="${response.town}"]`).addClass('selected');
}

export function form(form) {
    formId = form;
    currentTownPage = 0; isTownLoading = false; hasMoreTown = true;
    loadTowns();

    const formWidget = $(`#${formId}`);
    formWidget.find('#town-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isTownLoading && hasMoreTown) {
            loadTowns(currentTownPage + 1);
        }
    });

    formWidget.find('#town-list').on('click', 'div', function () {
        formWidget.find('#town-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#town-input').val($(this).data('id'));
    });
}

function loadTowns(page = 0) {
    if (isTownLoading) return;
    isTownLoading = true;
    $(`#${formId}`).find('#town-loading-indicator').show();
    $.ajax({
        url: c.baseUrl + c.apiUrl + '/location',
        type: 'GET',
        headers: getAuthHeader(),
        data: { page: page, size: 10 },
        success: (response) => {
            const list = $(`#${formId}`).find('#town-list');
            response.content.forEach(item => list.append(`<div data-id="${item.id}">ID: ${item.id} ${item.name ?? ''}</div>`));
            currentTownPage = page;
            hasMoreTown = currentTownPage + 1 < response.totalPages;
            isTownLoading = false;
            $(`#${formId}`).find('#town-loading-indicator').hide();
        },
        error: (xhr) => { isTownLoading = false; new ErrorNotify('Ошибка загрузки локаций', xhr.responseText); },
    });
}

