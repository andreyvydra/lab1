import $ from "jquery";
import {addObject, putObject} from "./objects";
import {getAuthHeader, loadForm, redirectIfAuthenticated} from "./common/utils";
import './common/common';
import './common/modal';
import {form, setValues} from "./forms/personForm";
import {PaginationTable} from "./common/pagination";
import {common} from "./common/common";
import WarningNotify from "./common/notifications/warningNotify";
import {errorNotifies} from "./common/error";
import InfoNotify from "./common/notifications/infoNotify";
import * as c from "./common/constants";

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


    $('#upload-xlsx-form').on('submit', function (event) {
        event.preventDefault();
        const fileInput = $('#xlsx-file')[0];
        if (fileInput.files.length > 0) {
            event.preventDefault();
            const formData = new FormData();
            formData.append('file', $('#xlsx-file')[0].files[0]);

            $.ajax({
                url: `${c.baseUrl}${c.apiUrl}/person/import`,
                headers: getAuthHeader(),
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    new InfoNotify("Импорт", response.message);
                },
                error: function(error) {
                    errorNotifies(error);
                }
            });
        } else {
            new WarningNotify("Ошибка", "Файл должен быть выбран!")
        }
    });

})
