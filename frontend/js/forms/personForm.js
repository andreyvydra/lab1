import $ from "jquery";
import * as c from "../common/constants";
import {getAuthHeader, redirectIfAuthenticated} from "../common/utils";
import ErrorNotify from "../common/notifications/errorNotify";
import '../../css/scrolls.css'


let currentPage = 0;
let isLoading = false;
let hasMore = true;

let formId = '';

export function setValues(id) {
    const formWidget = $(`#${formId}`);

    $.ajax({
        url: `${c.baseUrl}${c.apiUrl}/person/${id}`,
        type: "GET",
        headers: getAuthHeader(),
        success: (response) => {
            formWidget.find('input[name=is-changeable-input]').prop('checked', response.isChangeable);
            formWidget.find('#name-input').val(response.name);
            formWidget.find('#color-eyes-input').val(response.eyeColor);
            formWidget.find('#color-hair-input').val(response.hairColor);
            formWidget.find('#nationality-input').val(response.nationality);
            if (response.height !== undefined) {
                formWidget.find('#height-input').val(response.height);
            }
            formWidget.find('#location-input').val(response.location);
            formWidget.find('#passport-input').val(response.passportID);

            formWidget.find('.submit-button').data('id', id);

            updateSelectedItems(formWidget);

            formWidget.find('#location-list-container').off('scroll');
            formWidget.find('#location-list-container').on('scroll', function () {
                const container = $(this);
                if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isLoading && hasMore) {
                    loadLocations(currentPage + 1);
                    updateSelectedItems(formWidget);
                }
            });
        },
        error: (xhr) => {
            if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
            new ErrorNotify('Ошибка при загрузке данных дракона', xhr.responseText);
        },
    });
}

function updateSelectedItems(formWidget) {
    const selected = formWidget.find('#location-input').val();
    if (!selected) return;
    formWidget.find('#location-list div').removeClass('selected');
    formWidget.find(`#location-list div[data-id="${selected}"]`).addClass('selected');
}

export function form(form) {
    currentPage = 0;
    isLoading = false;
    hasMore = true;

    formId = form;

    const formWidget = $(`#${formId}`);

    loadLocations(currentPage);

    formWidget.find('#location-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isLoading && hasMore) {
            loadLocations(currentPage + 1);
        }
    });

    formWidget.find('#location-list').on('click', 'div', function () {
        formWidget.find('#location-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#location-input').val($(this).data('id'));
    });

}

function loadLocations(page = 0) {
    if (isLoading) return;
    isLoading = true;
    $(`#${formId}`).find('#location-loading-indicator').show();

    $.ajax({
        url: c.baseUrl + c.apiUrl + '/location',
        type: 'GET',
        data: {
            page: page,
            size: 10
        },
        headers: getAuthHeader(),
        success: function (response) {
            if (response.content.length > 0) {
                updateLocationList(response.content);
                currentPage = page;
            } else {
                hasMore = false;
            }
        },
        error: function (xhr, status, error) {
            new ErrorNotify('Ошибка при загрузке локаций', error);
        },
        complete: function () {
            isLoading = false;
            $(`#${formId}`).find('#location-loading-indicator').hide();
        }
    });
}

function updateLocationList(locations) {
    const locationList = $(`#${formId}`).find('#location-list');
    locations.forEach(location => {
        locationList.append(`
            <div data-id="${location.id}">
                ${location.name}
            </div>
        `);
    });
}
