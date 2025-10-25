import $ from "jquery";
import {addObject, putObject} from "./objects";
import {loadForm, redirectIfAuthenticated, getCookie, decodeJWT} from "./common/utils";
import './common/common';
import './common/modal';
import {form, setValues} from "./forms/organizationForm";
import {PaginationTable} from "./common/pagination";
import {common} from "./common/common";

const formPath = './forms/organization_form.html';

class OrganizationPaginationTable extends PaginationTable {
    addBodyTable(values) {
        const row = $('<tr>');
        const orderedKeys = [
            'id','isChangeable','user','name','officialAddress','annualTurnover','employeesCount','fullName','rating','type'
        ];
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
            'update-organization-form', function () {
                const modal = $('#update-modal');
                modal.css('display', 'block');
                $('#update-organization-form').data('id', id);

                $('#update-organization-form').on('submit', function (event) {
                    event.preventDefault();
                    const formData = getFormData(this);
                    putObject("/organizations", $(this).data('id'), formData);
                });

                form('update-organization-form');
                setValues(id);
            });
    }
}

function getFormData(formEl) {
    return {
        isChangeable: $(formEl).find('input[name="is-changeable-input"]').is(':checked'),
        name: $(formEl).find('#name-input').val(),
        officialAddressId: $(formEl).find('#official-address-input').val() || null,
        annualTurnover: $(formEl).find('#annual-turnover-input').val() || null,
        employeesCount: $(formEl).find('#employees-count-input').val(),
        fullName: $(formEl).find('#full-name-input').val() || null,
        rating: $(formEl).find('#rating-input').val() || null,
        type: $(formEl).find('#type-input').val() || null
    };
}

$(document).ready(function () {
    redirectIfAuthenticated();

    const table = new OrganizationPaginationTable("/organizations");
    common(table);

    loadForm(formPath, 'input-article', 'organization-form', null);
    form('organization-form');

    $('#organization-form').on('submit', function (event) {
        event.preventDefault();
        const formData = getFormData(this);
        addObject("/organizations", formData);
    });
});
