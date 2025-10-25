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
    $.ajax({
        url: `${c.baseUrl}${c.apiUrl}/product/${id}`,
        type: "GET",
        headers: getAuthHeader(),
        success: (response) => {
            formWidget.find('input[name=is-changeable-input]').prop('checked', response.isChangeable);
            formWidget.find('#name-input').val(response.name);
            formWidget.find('#coordinates-input').val(response.coordinates);
            formWidget.find('#owner-input').val(response.owner);
            formWidget.find('#manufacturer-input').val(response.manufacturer);
            formWidget.find('#unit-input').val(response.unitOfMeasure ?? '');
            formWidget.find('#price-input').val(response.price ?? '');
            formWidget.find('#manufacture-cost-input').val(response.manufactureCost ?? '');
            formWidget.find('#rating-input').val(response.rating);

            formWidget.find('.submit-button').data('id', id);
            updateSelectedItems(formWidget, response);

            attachInfiniteScroll(formWidget, response);
        },
        error: (xhr) => {
            if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
            new ErrorNotify('Ошибка при загрузке формы: продукт', xhr.responseText);
        },
    });
}

function attachInfiniteScroll(formWidget, response) {
    formWidget.find('#coordinates-list-container').off('scroll').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isCoordinatesLoading && hasMoreCoordinates) {
            loadCoordinates(currentCoordinatesPage + 1);
            updateSelectedItems(formWidget, response);
        }
    });
    formWidget.find('#owner-list-container').off('scroll').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isOwnerLoading && hasMoreOwner) {
            loadOwners(currentOwnerPage + 1);
            updateSelectedItems(formWidget, response);
        }
    });
    formWidget.find('#manufacturer-list-container').off('scroll').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isManufacturerLoading && hasMoreManufacturer) {
            loadManufacturers(currentManufacturerPage + 1);
            updateSelectedItems(formWidget, response);
        }
    });
}

function updateSelectedItems(formWidget, response) {
    formWidget.find('#coordinates-list div').removeClass('selected');
    formWidget.find(`#coordinates-list div[data-id="${response.coordinates}"]`).addClass('selected');

    formWidget.find('#owner-list div').removeClass('selected');
    formWidget.find(`#owner-list div[data-id="${response.owner}"]`).addClass('selected');

    formWidget.find('#manufacturer-list div').removeClass('selected');
    formWidget.find(`#manufacturer-list div[data-id="${response.manufacturer}"]`).addClass('selected');
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
        formWidget.find('#coordinates-input').val($(this).data('id'));
    });
    formWidget.find('#owner-list').on('click', 'div', function () {
        formWidget.find('#owner-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#owner-input').val($(this).data('id'));
    });
    formWidget.find('#manufacturer-list').on('click', 'div', function () {
        formWidget.find('#manufacturer-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#manufacturer-input').val($(this).data('id'));
    });
}

function loadCoordinates(page = 0) {
    if (isCoordinatesLoading) return;
    isCoordinatesLoading = true;
    $(`#${formId}`).find('#coordinates-loading-indicator').show();
    $.ajax({
        url: c.baseUrl + c.apiUrl + '/coordinates',
        type: 'GET',
        headers: getAuthHeader(),
        data: { page: page, size: 10 },
        success: (response) => {
            const list = $(`#${formId}`).find('#coordinates-list');
            response.content.forEach(item => list.append(`<div data-id="${item.id}">ID: ${item.id} (${item.x}; ${item.y})</div>`));
            currentCoordinatesPage = page;
            hasMoreCoordinates = currentCoordinatesPage + 1 < response.totalPages;
            isCoordinatesLoading = false;
            $(`#${formId}`).find('#coordinates-loading-indicator').hide();
        },
        error: (xhr) => { isCoordinatesLoading = false; new ErrorNotify('Ошибка загрузки координат', xhr.responseText); },
    });
}

function loadOwners(page = 0) {
    if (isOwnerLoading) return;
    isOwnerLoading = true;
    $(`#${formId}`).find('#owner-loading-indicator').show();
    $.ajax({
        url: c.baseUrl + c.apiUrl + '/person',
        type: 'GET',
        headers: getAuthHeader(),
        data: { page: page, size: 10 },
        success: (response) => {
            const list = $(`#${formId}`).find('#owner-list');
            response.content.forEach(item => list.append(`<div data-id="${item.id}">${item.name}</div>`));
            currentOwnerPage = page;
            hasMoreOwner = currentOwnerPage + 1 < response.totalPages;
            isOwnerLoading = false;
            $(`#${formId}`).find('#owner-loading-indicator').hide();
        },
        error: (xhr) => { isOwnerLoading = false; new ErrorNotify('Ошибка загрузки владельцев', xhr.responseText); },
    });
}

function loadManufacturers(page = 0) {
    if (isManufacturerLoading) return;
    isManufacturerLoading = true;
    $(`#${formId}`).find('#manufacturer-loading-indicator').show();
    $.ajax({
        url: c.baseUrl + c.apiUrl + '/organizations',
        type: 'GET',
        headers: getAuthHeader(),
        data: { page: page, size: 10 },
        success: (response) => {
            const list = $(`#${formId}`).find('#manufacturer-list');
            response.content.forEach(item => list.append(`<div data-id="${item.id}">${item.name}</div>`));
            currentManufacturerPage = page;
            hasMoreManufacturer = currentManufacturerPage + 1 < response.totalPages;
            isManufacturerLoading = false;
            $(`#${formId}`).find('#manufacturer-loading-indicator').hide();
        },
        error: (xhr) => { isManufacturerLoading = false; new ErrorNotify('Ошибка загрузки организаций', xhr.responseText); },
    });
}

