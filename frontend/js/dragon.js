import $ from "jquery";
import {addObject, main} from "./objects";
import {getAuthHeader, redirectIfAuthenticated} from "./common/utils";
import * as c from "./common/constants";
import '../css/scrolls.css'
import ErrorNotify from "./common/notifications/errorNotify";

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

$(document).ready(function() {
    redirectIfAuthenticated();
    main("/dragon");

    loadCoordinates();
    loadKillers();
    loadCaves();
    loadHeads();

    // Обработка скролла для координат
    $('#coordinates-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isCoordinatesLoading && hasMoreCoordinates) {
            loadCoordinates(currentCoordinatesPage + 1);
        }
    });

    // Обработка скролла для убийц
    $('#killer-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isKillerLoading && hasMoreKiller) {
            loadKillers(currentKillerPage + 1);
        }
    });

    // Обработка скролла для пещер
    $('#cave-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isCaveLoading && hasMoreCave) {
            loadCaves(currentCavePage + 1);
        }
    });

    // Обработка скролла для голов
    $('#head-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight - 10 && !isHeadLoading && hasMoreHead) {
            loadHeads(currentHeadPage + 1);
        }
    });

    // Обработка выбора координат
    $('#coordinates-list').on('click', 'div', function () {
        $('#coordinates-list div').removeClass('selected');
        $(this).addClass('selected');
        $('#coordinates-input').val($(this).data('id'));
    });

    // Обработка выбора убийцы
    $('#killer-list').on('click', 'div', function () {
        $('#killer-list div').removeClass('selected');
        $(this).addClass('selected');
        $('#killer-input').val($(this).data('id'));
    });

    // Обработка выбора пещеры
    $('#cave-list').on('click', 'div', function () {
        $('#cave-list div').removeClass('selected');
        $(this).addClass('selected');
        $('#cave-input').val($(this).data('id'));
    });

    // Обработка выбора головы
    $('#head-list').on('click', 'div', function () {
        $('#head-list div').removeClass('selected');
        $(this).addClass('selected');
        $('#head-input').val($(this).data('id'));
    });

    $('#form').on('submit', function (event) {
        event.preventDefault();

        const formData = {
            isChangeable: $('#form input[name="is-changeable-input"]').is(':checked'),
            name: $('#name-input').val(),
            coordinates: $('#coordinates-input').val(),
            killer: $('#killer-input').val(),
            cave: $('#cave-input').val(),
            head: $('#head-input').val(),
            character: $('#dragon-character-input').val(),
            age: $('#age-input').val(),
            speaking: $('#form input[name="speaking-input"]').is(':checked'),
            color: $('#color-input').val()
        };

        addObject("/dragon", formData);
    });
})

function loadCoordinates(page = 0) {
    if (isCoordinatesLoading) return;
    isCoordinatesLoading = true;
    $('#coordinates-loading-indicator').show();

    $.ajax({
        url: c.baseUrl + c.apiUrl + '/coordinates',
        type: 'GET',
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
            $('#coordinates-loading-indicator').hide();
        }
    });
}

function loadKillers(page = 0) {
    if (isKillerLoading) return;
    isKillerLoading = true;
    $('#killer-loading-indicator').show();

    $.ajax({
        url: c.baseUrl + c.apiUrl +  '/person',
        type: 'GET',
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
    const list = $(listId);
    items.forEach(item => {
        list.append(`
            <div data-id="${item.id}">
                ${item.name ||  item.depth || item.numberOfTreasures || item.size || item.x + " " + item.y }
            </div>
        `);
    });
}