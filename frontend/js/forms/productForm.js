import $ from "jquery";
import * as c from "../common/constants";
import {getAuthHeader, redirectIfAuthenticated} from "../common/utils";
import ErrorNotify from "../common/notifications/errorNotify";
import '../../css/scrolls.css'

let formId = '';

let currentCoordinatesPage = 0, isCoordinatesLoading = false, hasMoreCoordinates = true;
let currentOwnerPage = 0, isOwnerLoading = false, hasMoreOwner = true;
let currentManufacturerPage = 0, isManufacturerLoading = false, hasMoreManufacturer = true;

export function setValues(id) {
    const formWidget = $(`#${formId}`);
    // mark fields dirty on user changes to avoid overwriting with async prefill
    formWidget.find('input, select, textarea').off('input.change').on('input change', function () {
        $(this).data('dirty', true);
    });
    $.ajax({
        url: `${c.baseUrl}${c.apiUrl}/product/${id}`,
        type: "GET",
        headers: getAuthHeader(),
        success: (response) => {
            const setIfPristine = (sel, setter) => {
                const el = formWidget.find(sel);
                if (!el.data('dirty')) setter(el);
            };
            setIfPristine('input[name=is-changeable-input]', el => el.prop('checked', response.isChangeable));
            setIfPristine('#name-input', el => el.val(response.name));
            setIfPristine('#coordinates-input', el => el.val(response.coordinates));
            setIfPristine('#owner-input', el => el.val(response.owner));
            setIfPristine('#manufacturer-input', el => el.val(response.manufacturer));
            setIfPristine('#unit-input', el => el.val(response.unitOfMeasure ?? ''));
            setIfPristine('#price-input', el => el.val(response.price ?? ''));
            setIfPristine('#manufacture-cost-input', el => el.val(response.manufactureCost ?? ''));
            setIfPristine('#rating-input', el => el.val(response.rating));

            formWidget.find('.submit-button').data('id', id);
            updateSelectedItems(formWidget);

            attachInfiniteScroll(formWidget);
        },
        error: (xhr) => {
            if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
            new ErrorNotify('Ошибка при загрузке формы: продукт', xhr.responseText);
        },
    });
}

function attachInfiniteScroll(formWidget) {
    formWidget.find('#coordinates-list-container').off('scroll').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isCoordinatesLoading && hasMoreCoordinates) {
            loadCoordinates(currentCoordinatesPage + 1);
            updateSelectedItems(formWidget);
        }
    });
    formWidget.find('#owner-list-container').off('scroll').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isOwnerLoading && hasMoreOwner) {
            loadOwners(currentOwnerPage + 1);
            updateSelectedItems(formWidget);
        }
    });
    formWidget.find('#manufacturer-list-container').off('scroll').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isManufacturerLoading && hasMoreManufacturer) {
            loadManufacturers(currentManufacturerPage + 1);
            updateSelectedItems(formWidget);
        }
    });
}

function updateSelectedItems(formWidget) {
    const selCoord = formWidget.find('#coordinates-input').val();
    if (selCoord) {
        formWidget.find('#coordinates-list div').removeClass('selected');
        formWidget.find(`#coordinates-list div[data-id="${selCoord}"]`).addClass('selected');
    }

    const selOwner = formWidget.find('#owner-input').val();
    if (selOwner) {
        formWidget.find('#owner-list div').removeClass('selected');
        formWidget.find(`#owner-list div[data-id="${selOwner}"]`).addClass('selected');
    }

    const selMan = formWidget.find('#manufacturer-input').val();
    if (selMan) {
        formWidget.find('#manufacturer-list div').removeClass('selected');
        formWidget.find(`#manufacturer-list div[data-id="${selMan}"]`).addClass('selected');
    }
}

export function form(form) {
    formId = form;
    currentCoordinatesPage = 0; isCoordinatesLoading = false; hasMoreCoordinates = true;
    currentOwnerPage = 0; isOwnerLoading = false; hasMoreOwner = true;
    currentManufacturerPage = 0; isManufacturerLoading = false; hasMoreManufacturer = true;

    loadCoordinates();
    loadOwners();
    loadManufacturers();

    const formWidget = $(`#${formId}`);

    formWidget.find('#coordinates-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isCoordinatesLoading && hasMoreCoordinates) {
            loadCoordinates(currentCoordinatesPage + 1);
        }
    });
    formWidget.find('#owner-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isOwnerLoading && hasMoreOwner) {
            loadOwners(currentOwnerPage + 1);
        }
    });
    formWidget.find('#manufacturer-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isManufacturerLoading && hasMoreManufacturer) {
            loadManufacturers(currentManufacturerPage + 1);
        }
    });

    formWidget.find('#coordinates-list').on('click', 'div', function () {
        formWidget.find('#coordinates-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#coordinates-input').val($(this).data('id')).data('dirty', true);
    });
    formWidget.find('#owner-list').on('click', 'div', function () {
        formWidget.find('#owner-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#owner-input').val($(this).data('id')).data('dirty', true);
    });
    formWidget.find('#manufacturer-list').on('click', 'div', function () {
        formWidget.find('#manufacturer-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#manufacturer-input').val($(this).data('id')).data('dirty', true);
    });
}

function loadCoordinates(page = 0) {
    if (isCoordinatesLoading) return;
    isCoordinatesLoading = true;
    const formWidgetCoords = $(`#${formId}`);
    formWidgetCoords.find('#coordinates-loading-indicator').show();
    $.ajax({
        url: c.baseUrl + c.apiUrl + '/coordinates',
        type: 'GET',
        headers: getAuthHeader(),
        data: { page: page, size: 10 },
        success: (response) => {
            const list = formWidgetCoords.find('#coordinates-list');
            response.content.forEach(item => list.append(`<div data-id="${item.id}">ID: ${item.id} (${item.x}; ${item.y})</div>`));
            // Ensure selected coordinates is highlighted after items load
            const selCoord = formWidgetCoords.find('#coordinates-input').val();
            if (selCoord) {
                list.find('div').removeClass('selected');
                list.find(`div[data-id="${selCoord}"]`).addClass('selected');
            }
            currentCoordinatesPage = page;
            hasMoreCoordinates = currentCoordinatesPage + 1 < response.totalPages;
            isCoordinatesLoading = false;
            formWidgetCoords.find('#coordinates-loading-indicator').hide();
        },
        error: (xhr) => { isCoordinatesLoading = false; new ErrorNotify('Ошибка загрузки координат', xhr.responseText); },
    });
}

function loadOwners(page = 0) {
    if (isOwnerLoading) return;
    isOwnerLoading = true;
    const formWidgetOwner = $(`#${formId}`);
    formWidgetOwner.find('#owner-loading-indicator').show();
    $.ajax({
        url: c.baseUrl + c.apiUrl + '/person',
        type: 'GET',
        headers: getAuthHeader(),
        data: { page: page, size: 10 },
        success: (response) => {
            const list = formWidgetOwner.find('#owner-list');
            response.content.forEach(item => list.append(`<div data-id="${item.id}">${item.name}</div>`));
            // Ensure selected owner is highlighted after items load
            const selOwner = formWidgetOwner.find('#owner-input').val();
            if (selOwner) {
                list.find('div').removeClass('selected');
                list.find(`div[data-id="${selOwner}"]`).addClass('selected');
            }
            currentOwnerPage = page;
            hasMoreOwner = currentOwnerPage + 1 < response.totalPages;
            isOwnerLoading = false;
            formWidgetOwner.find('#owner-loading-indicator').hide();
        },
        error: (xhr) => { isOwnerLoading = false; new ErrorNotify('Ошибка загрузки владельцев', xhr.responseText); },
    });
}

function loadManufacturers(page = 0) {
    if (isManufacturerLoading) return;
    isManufacturerLoading = true;
    const formWidgetMan = $(`#${formId}`);
    formWidgetMan.find('#manufacturer-loading-indicator').show();
    $.ajax({
        url: c.baseUrl + c.apiUrl + '/organizations',
        type: 'GET',
        headers: getAuthHeader(),
        data: { page: page, size: 10 },
        success: (response) => {
            const list = formWidgetMan.find('#manufacturer-list');
            response.content.forEach(item => list.append(`<div data-id="${item.id}">${item.name}</div>`));
            // Ensure selected manufacturer is highlighted after items load
            const selMan = formWidgetMan.find('#manufacturer-input').val();
            if (selMan) {
                list.find('div').removeClass('selected');
                list.find(`div[data-id="${selMan}"]`).addClass('selected');
            }
            currentManufacturerPage = page;
            hasMoreManufacturer = currentManufacturerPage + 1 < response.totalPages;
            isManufacturerLoading = false;
            formWidgetMan.find('#manufacturer-loading-indicator').hide();
        },
        error: (xhr) => { isManufacturerLoading = false; new ErrorNotify('Ошибка загрузки организаций', xhr.responseText); },
    });
}
