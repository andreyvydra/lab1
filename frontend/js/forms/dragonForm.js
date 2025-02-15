import $ from "jquery";
import * as c from "../common/constants";
import {getAuthHeader, redirectIfAuthenticated} from "../common/utils";
import ErrorNotify from "../common/notifications/errorNotify";
import '../../css/scrolls.css'


let currentCoordinatesPage = 0;
let isCoordinatesLoading = false;
let hasMoreCoordinates = true;

let currentKillerPage = 0;
let isKillerLoading = false;
let hasMoreKiller = true;

let currentCavePage = 0;
let isCaveLoading = false;
let hasMoreCave = true;

let currentHeadPage = 0;
let isHeadLoading = false;
let hasMoreHead = true;

let formId = '';

export function setValues(id) {
    const formWidget = $(`#${formId}`);

    $.ajax({
        url: `${c.baseUrl}${c.apiUrl}/dragon/${id}`,
        type: "GET",
        headers: getAuthHeader(),
        success: (response) => {
            formWidget.find('input[name=is-changeable-input]').prop('checked', response.isChangeable);
            formWidget.find('#name-input').val(response.name);
            formWidget.find('#coordinates-input').val(response.coordinates);
            formWidget.find('#killer-input').val(response.killer);
            formWidget.find('#cave-input').val(response.cave);
            formWidget.find('#age-input').val(response.age);
            formWidget.find('input[name="speaking-input"]').prop('checked', response.speaking);
            formWidget.find('#color-input').val(response.color);
            formWidget.find('#dragon-character-input').val(response.character);
            formWidget.find('#head-input').val(response.head);

            formWidget.find('.submit-button').data('id', id);
            updateSelectedItems(formWidget, response);
        },
        error: (xhr) => {
            if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
            new ErrorNotify('Ошибка при загрузке данных дракона', xhr.responseText);
        },
    });
}

function updateSelectedItems(formWidget, response) {
    console.log(response);

    // formWidget.find('#coordinates-list div').removeClass('selected');
    console.log(formWidget.find(`#coordinates-list div[data-id="${response.coordinates}"]`))
    formWidget.find(`#coordinates-list div[data-id="${response.coordinates}"]`).addClass('selected');

    // formWidget.find('#killer-list div').removeClass('selected');
    console.log(formWidget.find(`#killer-list div[data-id="${response.killer}"]`))
    formWidget.find(`#killer-list div[data-id="${response.killer}"]`).addClass('selected');

    // formWidget.find('#cave-list div').removeClass('selected');
    console.log(formWidget.find(`#cave-list div[data-id="${response.cave}"]`))
    formWidget.find(`#cave-list div[data-id="${response.cave}"]`).addClass('selected');

    // formWidget.find('#head-list div').removeClass('selected');
    console.log(formWidget.find(`#head-list div[data-id="${response.head}"]`))
    formWidget.find(`#head-list div[data-id="${response.head}"]`).addClass('selected');
}

export function form(form) {
    currentCoordinatesPage = 0;
    isCoordinatesLoading = false;
    hasMoreCoordinates = true;

    currentKillerPage = 0;
    isKillerLoading = false;
    hasMoreKiller = true;

    currentCavePage = 0;
    isCaveLoading = false;
    hasMoreCave = true;

    currentHeadPage = 0;
    isHeadLoading = false;
    hasMoreHead = true;

    formId = form;

    loadCoordinates();
    loadKillers();
    loadCaves();
    loadHeads();

    const formWidget = $(`#${formId}`);

    // Обработка скролла для координат
    formWidget.find(`#coordinates-list-container`).on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isCoordinatesLoading && hasMoreCoordinates) {
            loadCoordinates(currentCoordinatesPage + 1);
        }
    });

    // Обработка скролла для убийц
    formWidget.find('#killer-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isKillerLoading && hasMoreKiller) {
            loadKillers(currentKillerPage + 1);
        }
    });

    // Обработка скролла для пещер
    formWidget.find('#cave-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isCaveLoading && hasMoreCave) {
            loadCaves(currentCavePage + 1);
        }
    });

    // Обработка скролла для голов
    formWidget.find('#head-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isHeadLoading && hasMoreHead) {
            loadHeads(currentHeadPage + 1);
        }
    });

    // Обработка выбора координат
    formWidget.find('#coordinates-list').on('click', 'div', function () {
        formWidget.find('#coordinates-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#coordinates-input').val($(this).data('id'));
    });

    // Обработка выбора убийцы
    formWidget.find('#killer-list').on('click', 'div', function () {
        formWidget.find('#killer-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#killer-input').val($(this).data('id'));
    });

    // Обработка выбора пещеры
    formWidget.find('#cave-list').on('click', 'div', function () {
        formWidget.find('#cave-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#cave-input').val($(this).data('id'));
    });

    // Обработка выбора головы
    formWidget.find('#head-list').on('click', 'div', function () {
        formWidget.find('#head-list div').removeClass('selected');
        $(this).addClass('selected');
        formWidget.find('#head-input').val($(this).data('id'));
    });

}

function loadCoordinates(page = 0) {
    if (isCoordinatesLoading) return;
    isCoordinatesLoading = true;
    $(`#${formId}`).find('#coordinates-loading-indicator').show();

    $.ajax({
        url: c.baseUrl + c.apiUrl + '/coordinates',
        type: 'GET',
        async: false,
        data: {
            page: page,
            size: 10
        },
        headers: getAuthHeader(),
        success: function (response) {
            if (response.content.length > 0) {
                updateList('#coordinates-list', response.content);
                currentCoordinatesPage = page;
            } else {
                hasMoreCoordinates = false;
            }
        },
        error: function (xhr, status, error) {
            new ErrorNotify('Ошибка при загрузке координат', error);
        },
        complete: function () {
            isCoordinatesLoading = false;
            $(`#${formId}`).find('#coordinates-loading-indicator').hide();
        }
    });
}

function loadKillers(page = 0) {
    if (isKillerLoading) return;
    isKillerLoading = true;
    $('#killer-loading-indicator').show();

    $.ajax({
        url: c.baseUrl + c.apiUrl + '/person',
        type: 'GET',
        async: false,
        data: {
            page: page,
            size: 10
        },
        headers: getAuthHeader(),
        success: function (response) {
            if (response.content.length > 0) {
                updateList('#killer-list', response.content);
                currentKillerPage = page;
            } else {
                hasMoreKiller = false;
            }
        },
        error: function (xhr, status, error) {
            new ErrorNotify('Ошибка при загрузке убийц', error);
        },
        complete: function () {
            isKillerLoading = false;
            $('#killer-loading-indicator').hide();
        }
    });
}

function loadCaves(page = 0) {
    if (isCaveLoading) return;
    isCaveLoading = true;
    $('#cave-loading-indicator').show();

    $.ajax({
        url: c.baseUrl + c.apiUrl + '/dragonCave',
        type: 'GET',
        async: false,
        data: {
            page: page,
            size: 10
        },
        headers: getAuthHeader(),
        success: function (response) {
            if (response.content.length > 0) {
                updateList('#cave-list', response.content);
                currentCavePage = page;
            } else {
                hasMoreCave = false;
            }
        },
        error: function (xhr, status, error) {
            new ErrorNotify('Ошибка при загрузке пещер', error);
        },
        complete: function () {
            isCaveLoading = false;
            $('#cave-loading-indicator').hide();
        }
    });
}

function loadHeads(page = 0) {
    if (isHeadLoading) return;
    isHeadLoading = true;
    $('#head-loading-indicator').show();

    $.ajax({
        url: c.baseUrl + c.apiUrl + '/dragonHead',
        type: 'GET',
        async: false,
        data: {
            page: page,
            size: 10
        },
        headers: getAuthHeader(),
        success: function (response) {
            if (response.content.length > 0) {
                updateList('#head-list', response.content);
                currentHeadPage = page;
            } else {
                hasMoreHead = false;
            }
        },
        error: function (xhr, status, error) {
            new ErrorNotify('Ошибка при загрузке голов', error);
        },
        complete: function () {
            isHeadLoading = false;
            $('#head-loading-indicator').hide();
        }
    });
}

function updateList(listId, items) {
    const list = $(`#${formId}`).find(listId);
    items.forEach(item => {
        list.append(`
            <div data-id="${item.id}">
                ${item.name || item.depth || item.numberOfTreasures || item.size || item.x + " " + item.y}
            </div>
        `);
    });
}