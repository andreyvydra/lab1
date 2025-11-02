import $ from "jquery";
import * as c from "../common/constants";
import {getAuthHeader, redirectIfAuthenticated} from "../common/utils";
import ErrorNotify from "../common/notifications/errorNotify";
import '../../css/scrolls.css'

let formId = '';
let currentTownPage = 0, isTownLoading = false, hasMoreTown = true;

export function setValues(id) {
    const formWidget = $(`#${formId}`);
    formWidget.find('input, select, textarea').off('input.change').on('input change', function () {
        $(this).data('dirty', true);
    });
    $.ajax({
        url: `${c.baseUrl}${c.apiUrl}/addresses/${id}`,
        type: "GET",
        headers: getAuthHeader(),
        success: (response) => {
            const setIfPristine = (sel, setter) => {
                const el = formWidget.find(sel);
                if (!el.data('dirty')) setter(el);
            };
            setIfPristine('input[name=is-changeable-input]', el => el.prop('checked', response.isChangeable));
            setIfPristine('#street-input', el => el.val(response.street ?? ''));
            setIfPristine('#town-input', el => el.val(response.town ?? ''));

            formWidget.find('.submit-button').data('id', id);
            updateSelectedItems(formWidget);
            attachInfiniteScroll(formWidget);
        },
        error: (xhr) => {
            if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
            new ErrorNotify('Ошибка при загрузке адреса', xhr.responseText);
        },
    });
}

function attachInfiniteScroll(formWidget) {
    formWidget.find('#town-list-container').off('scroll').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isTownLoading && hasMoreTown) {
            loadTowns(currentTownPage + 1);
            updateSelectedItems(formWidget);
        }
    });
}

function updateSelectedItems(formWidget) {
    const selected = formWidget.find('#town-input').val();
    if (!selected) return;
    formWidget.find('#town-list div').removeClass('selected');
    formWidget.find(`#town-list div[data-id="${selected}"]`).addClass('selected');
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
        formWidget.find('#town-input').val($(this).data('id')).data('dirty', true);
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
            // Ensure selected town is highlighted after items load
            const selTown = $(`#${formId}`).find('#town-input').val();
            if (selTown) {
                list.find('div').removeClass('selected');
                list.find(`div[data-id="${selTown}"]`).addClass('selected');
            }
            currentTownPage = page;
            hasMoreTown = currentTownPage + 1 < response.totalPages;
            isTownLoading = false;
            $(`#${formId}`).find('#town-loading-indicator').hide();
        },
        error: (xhr) => { isTownLoading = false; new ErrorNotify('Ошибка загрузки локаций', xhr.responseText); },
    });
}
