import $ from "jquery";
import './common/modal';
import {addObject, putObject} from "./objects";
import {loadForm, redirectIfAuthenticated} from "./common/utils";
import {common} from "./common/common";
import {form, setValues} from "./forms/locationForm";
import {PaginationTable} from "./common/pagination";

const formPath = './forms/location_form.html'

class LocationPaginationTable extends PaginationTable {
    handleUpdate(id) {
        loadForm(formPath, 'update-modal-content',
            'update-location-form', function (formId) {
                const modal = $('#update-modal');
                modal.css('display', 'block');
                $('#update-location-form').data('id', id);

                $('#update-location-form').on('submit', function (event) {
                    event.preventDefault();
                    const formData = getFormData(this);
                    putObject("/location", $(this).data('id'), formData);
                });

                form('update-location-form');
                setValues(id);
            });
    }
}

function getFormData(form) {
    return {
        isChangeable: $(form).find('input[name="is-changeable-input"]').is(':checked'),
        x: $(form).find('#x-input').val(),
        y: $(form).find('#y-input').val(),
        name: $(form).find('#name-input').val()
    };
}

$(document).ready(function () {
    redirectIfAuthenticated();

    // Table and it's functions
    const table = new LocationPaginationTable("/location");
    common(table);

    loadForm(formPath, 'input-article', 'location-form', null);
    form('location-form');

    $(`#location-form`).on('submit', function (event) {
        event.preventDefault();
        const formData = getFormData(this);
        addObject("/location", formData);
    });

})
