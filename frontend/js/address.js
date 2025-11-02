import $ from "jquery";
import {addObject, putObject} from "./objects";
import {loadForm, redirectIfAuthenticated, getCookie, decodeJWT} from "./common/utils";
import './common/common';
import './common/modal';
import {form, setValues} from "./forms/addressForm";
import {PaginationTable} from "./common/pagination";
import {common} from "./common/common";

const formPath = './forms/address_form.html';

class AddressPaginationTable extends PaginationTable {
    addBodyTable(values) {
        const row = $('<tr>');
        const orderedKeys = ['id','isChangeable','user','street','town'];
        orderedKeys.forEach(k => row.append($('<td>').text(values[k])));
        const { payload } = decodeJWT(getCookie("access_token")) || {};
        const actionsCell = $('<td>');
        const isOwner = payload && values.user === payload.id;
        const isAdmin = payload && payload.role === "ROLE_ADMIN";
        if (isOwner || (isAdmin && values.isChangeable)) {
            const updateButton = $('<button>').html('<div>').addClass('action-button update-button').on('click', () => this.handleUpdate(values.id));
            const deleteButton = $('<button>').html('<div>').addClass('action-button delete-button').on('click', () => this.handleDelete(values.id));
            actionsCell.append(updateButton, deleteButton);
        }
        row.append(actionsCell);
        this.table.append(row);
    }
    handleUpdate(id) {
        loadForm(formPath, 'update-modal-content',
            'update-address-form', function () {
                const modal = $('#update-modal');
                modal.css('display', 'block');
                $('#update-address-form').data('id', id);

                $('#update-address-form').on('submit', function (event) {
                    event.preventDefault();
                    const formData = getFormData(this);
                    putObject("/addresses", $(this).data('id'), formData);
                });

                form('update-address-form');
                setValues(id);
            });
    }
}

function getFormData(formEl) {
    return {
        isChangeable: $(formEl).find('input[name="is-changeable-input"]').is(':checked'),
        street: $(formEl).find('#street-input').val() || null,
        townId: $(formEl).find('#town-list div.selected').data('id') || $(formEl).find('#town-input').val() || null
    };
}

$(document).ready(function () {
    redirectIfAuthenticated();

    const table = new AddressPaginationTable("/addresses");
    common(table);

    loadForm(formPath, 'input-article', 'address-form', null);
    form('address-form');

    $('#address-form').on('submit', function (event) {
        event.preventDefault();
        const formData = getFormData(this);
        addObject("/addresses", formData);
    });
});
