import $ from "jquery";
import * as c from "../common/constants";
import {getAuthHeader, redirectIfAuthenticated} from "../common/utils";
import ErrorNotify from "../common/notifications/errorNotify";
import '../../css/scrolls.css'

let formId = '';

let currentAddressPage = 0, isAddressLoading = false, hasMoreAddress = true;

export function setValues(id) {
    const formWidget = $(`#${formId}`);
    $.ajax({
        url: `${c.baseUrl}${c.apiUrl}/organizations/${id}`,
        type: "GET",
        headers: getAuthHeader(),
        success: (response) => {
            formWidget.find('input[name=is-changeable-input]').prop('checked', response.isChangeable);
            formWidget.find('#name-input').val(response.name);
            formWidget.find('#employees-count-input').val(response.employeesCount);
            formWidget.find('#annual-turnover-input').val(response.annualTurnover ?? '');
            formWidget.find('#full-name-input').val(response.fullName ?? '');
            formWidget.find('#rating-input').val(response.rating ?? '');
            formWidget.find('#type-input').val(response.type ?? '');
            formWidget.find('#official-address-input').val(response.officialAddress ?? '');

            formWidget.find('.submit-button').data('id', id);
            updateSelectedItems(formWidget, response);
            attachInfiniteScroll(formWidget, response);
        },
        error: (xhr) => {
            if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
            new ErrorNotify('Ошибка при загрузке организации', xhr.responseText);
        },
    });
}

function attachInfiniteScroll(formWidget, response) {
    formWidget.find('#official-address-list-container').off('scroll').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isAddressLoading && hasMoreAddress) {
            loadAddresses(currentAddressPage + 1);
            updateSelectedItems(formWidget, response);
        }
    });
}

function updateSelectedItems(formWidget, response) {
    formWidget.find('#official-address-list div').removeClass('selected');
    formWidget.find(`#official-address-list div[data-id="${response.officialAddress}"]`).addClass('selected');
}

export function form(form) {
    formId = form;
    currentAddressPage = 0; isAddressLoading = false; hasMoreAddress = true;
    loadAddresses();

    const formWidget = $(`#${formId}`);
    formWidget.find('#official-address-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isAddressLoading && hasMoreAddress) {
            loadAddresses(currentAddressPage + 1);
        }
    });

    formWidget.find('#official-address-list').on('click', 'div', function () {
        formWidget.find('#official-address-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#official-address-input').val($(this).data('id'));
    });
}

function loadAddresses(page = 0) {
    if (isAddressLoading) return;
    isAddressLoading = true;
    $(`#${formId}`).find('#official-address-loading-indicator').show();
    $.ajax({
        url: c.baseUrl + c.apiUrl + '/addresses',
        type: 'GET',
        headers: getAuthHeader(),
        data: { page: page, size: 10 },
        success: (response) => {
            const list = $(`#${formId}`).find('#official-address-list');
            response.content.forEach(item => list.append(`<div data-id="${item.id}">ID: ${item.id} ${item.street ?? ''}</div>`));
            currentAddressPage = page;
            hasMoreAddress = currentAddressPage + 1 < response.totalPages;
            isAddressLoading = false;
            $(`#${formId}`).find('#official-address-loading-indicator').hide();
        },
        error: (xhr) => { isAddressLoading = false; new ErrorNotify('Ошибка загрузки адресов', xhr.responseText); },
    });
}
