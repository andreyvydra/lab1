import $ from "jquery";
import {addObject, putObject} from "./objects";
import {loadForm, redirectIfAuthenticated} from "./common/utils";
import './common/common';
import './common/modal';
import {form, setValues} from "./forms/personForm";
import {PaginationTable} from "./common/pagination";
import {common} from "./common/common";

const formPath = './forms/person_form.html'

class PersonPaginationTable extends PaginationTable {
    handleUpdate(id) {
        loadForm(formPath, 'update-modal-content',
            'update-person-form', function (formId) {
                const modal = $('#update-modal');
                modal.css('display', 'block');
                $('#update-person-form').data('id', id);

                $('#update-person-form').on('submit', function (event) {
                    event.preventDefault();
                    const formData = getFormData(this);
                    putObject("/person", $(this).data('id'), formData);
                });

                form('update-person-form');
                setValues(id);
            });
    }
}

function getFormData(form) {
    return  {
        isChangeable: $(form).find('input[name="is-changeable-input"]').is(':checked'),
        name: $(form).find('#name-input').val(),
        eyeColor: $(form).find('#color-eyes-input').val(),
        hairColor: $(form).find('#color-hair-input').val(),
        nationality: $(form).find('#nationality-input').val(),
        location: $(form).find('#location-input').val(),
        passportID: $(form).find('#passport-input').val()
    };
}


$(document).ready(function () {
    redirectIfAuthenticated();

    // Table and it's functions
    const table = new PersonPaginationTable("/person");
    common(table);

    loadForm(formPath, 'input-article', 'person-form', null);
    form('person-form');

    $(`#person-form`).on('submit', function (event) {
        event.preventDefault();
        const formData = getFormData(this);
        addObject("/person", formData);
    });

})
