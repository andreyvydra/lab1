import $ from "jquery";
import * as c from "../common/constants";
import {getAuthHeader, redirectIfAuthenticated} from "../common/utils";
import ErrorNotify from "../common/notifications/errorNotify";
import '../../css/scrolls.css'

let formId = '';

let currentAddressPage = 0, isAddressLoading = false, hasMoreAddress = true;

export function setValues(id) {
    const formWidget = $(`#${formId}`);
    formWidget.find('input, select, textarea').off('input.change').on('input change', function () {
        $(this).data('dirty', true);
    });
    $.ajax({
        url: `${c.baseUrl}${c.apiUrl}/organizations/${id}`,
        type: "GET",
        headers: getAuthHeader(),
        success: (response) => {
            const setIfPristine = (sel, setter) => {
                const el = formWidget.find(sel);
                if (!el.data('dirty')) setter(el);
            };
            setIfPristine('input[name=is-changeable-input]', el => el.prop('checked', response.isChangeable));
            setIfPristine('#name-input', el => el.val(response.name));
            setIfPristine('#employees-count-input', el => el.val(response.employeesCount));
            setIfPristine('#annual-turnover-input', el => el.val(response.annualTurnover ?? ''));
            setIfPristine('#full-name-input', el => el.val(response.fullName ?? ''));
            setIfPristine('#rating-input', el => el.val(response.rating ?? ''));
            setIfPristine('#type-input', el => el.val(response.type ?? ''));
            setIfPristine('#official-address-input', el => el.val(response.officialAddress ?? ''));

            formWidget.find('.submit-button').data('id', id);
            updateSelectedItems(formWidget);
            attachInfiniteScroll(formWidget);
        },
        error: (xhr) => {
            if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
            new ErrorNotify('Ошибка при загрузке организации', xhr.responseText);
        },
    });
}

function attachInfiniteScroll(formWidget) {
    formWidget.find('#official-address-list-container').off('scroll').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isAddressLoading && hasMoreAddress) {
            loadAddresses(currentAddressPage + 1);
            updateSelectedItems(formWidget);
        }
    });
}

function updateSelectedItems(formWidget) {
    const selected = formWidget.find('#official-address-input').val();
    if (!selected) return;
    formWidget.find('#official-address-list div').removeClass('selected');
    formWidget.find(`#official-address-list div[data-id="${selected}"]`).addClass('selected');
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
        formWidget.find('#official-address-input').val($(this).data('id')).data('dirty', true);
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
            // Ensure selected address is highlighted after items load
            const selAddr = $(`#${formId}`).find('#official-address-input').val();
            if (selAddr) {
                list.find('div').removeClass('selected');
                list.find(`div[data-id="${selAddr}"]`).addClass('selected');
            }
            currentAddressPage = page;
            hasMoreAddress = currentAddressPage + 1 < response.totalPages;
            isAddressLoading = false;
            $(`#${formId}`).find('#official-address-loading-indicator').hide();
        },
        error: (xhr) => { isAddressLoading = false; new ErrorNotify('Ошибка загрузки адресов', xhr.responseText); },
    });
}
