import $ from "jquery";
import {addObject, main} from "./objects";
import {getAuthHeader, redirectIfAuthenticated} from "./common/utils";
import * as c from "./common/constants";
import ErrorNotify from "./common/notifications/errorNotify";
import '../css/scrolls.css'

let currentPage = 0;
let isLoading = false;
let hasMore = true;


$(document).ready(function() {
    redirectIfAuthenticated();
    main("/person");

    loadLocations();

    $('#location-list-container').on('scroll', function () {
        const container = $(this);
        if (container.scrollTop() + container.innerHeight() >= container[0].scrollHeight && !isLoading && hasMore) {
            loadLocations(currentPage + 1);
        }
    });

    $('#location-list').on('click', 'div', function () {
        $('#location-list div').removeClass('selected');
        $(this).addClass('selected');
        $('#location-input').val($(this).data('id'));
    });

    $('#form').on('submit', function (event) {
        event.preventDefault();

        const formData = {
            isChangeable: $('#form input[name="is-changeable-input"]').is(':checked'),
            name: $('#name-input').val(),
            eyeColor: $('#color-eyes-input').val(),
            hairColor: $('#color-hair-input').val(),
            nationality: $('#nationality-input').val(),
            location: $('#location-input').val(),
            passportID: $('#passport-input').val()
        };

        addObject("/person", formData)
    });

})

function loadLocations(page = 0) {
    if (isLoading) return;
    isLoading = true;
    $('#location-loading-indicator').show();

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
            $('#location-loading-indicator').hide();
        }
    });
}

function updateLocationList(locations) {
    const locationList = $('#location-list');
    locations.forEach(location => {
        locationList.append(`
            <div data-id="${location.id}">
                ${location.name}
            </div>
        `);
    });
}