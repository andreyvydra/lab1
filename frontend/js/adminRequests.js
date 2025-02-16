import {PaginationTable} from "./common/pagination";
import {decodeJWT, getAuthHeader, getCookie, loadForm, redirectIfAuthenticated} from "./common/utils";
import $ from "jquery";
import {addObject} from "./objects";
import {form} from "./forms/coordinatesForm";
import {common} from "./common/common";
import {apiUrl, baseUrl} from "./common/constants";
import InfoNotify from "./common/notifications/infoNotify";
import ErrorNotify from "./common/notifications/errorNotify";

class AdminRequestsPaginationTable extends PaginationTable {
    addBodyTable(values) {
        const row = $('<tr>');

        for (const key in values) {
            if (values.hasOwnProperty(key)) {
                const cell = $('<td>').text(values[key]);
                row.append(cell);
            }
        }

        const {_, payload, __} = decodeJWT(getCookie("access_token"));
        const actionsCell = $('<td>');
        const isAdmin = payload.role === "ROLE_ADMIN";

        if (isAdmin && values.status === "PENDING") {
            const approveButton = $('<button>')
                .text('Принять')
                .addClass('approve-button')
                .on('click', () => this.handleApprove(values.id));

            const rejectButton = $('<button>')
                .text('Отказать')
                .addClass('reject-button')
                .on('click', () => this.handleReject(values.id));

            actionsCell.append(approveButton, rejectButton);
        }

        row.append(actionsCell);
        this.table.append(row);
    }

    handleApprove(requestId) {
        fetch(`${baseUrl}${apiUrl}/admin-requests/${requestId}/approve`, {
            method: 'PUT',
            headers: getAuthHeader()
        })
            .then(response => {
                if (response.ok) {
                    new InfoNotify('Заявка успешно принята!');
                    this.getPage();
                } else {
                    new ErrorNotify('Ошибка при принятии заявки.');
                }
            })
            .catch(error => {
                console.error('Ошибка:', error);
            });
    }

    handleReject(requestId) {
        fetch(`${baseUrl}${apiUrl}/admin-requests/${requestId}/approve`, {
            method: 'PUT',
            headers: getAuthHeader()
        })
            .then(response => {
                if (response.ok) {
                    new InfoNotify('Заявка успешно отклонена!');
                    this.getPage();
                } else {
                    new ErrorNotify('Ошибка при отклонении заявки.');
                }
            })
            .catch(error => {
                console.error('Ошибка:', error);
            });
    }
}

$(document).ready(function () {
    redirectIfAuthenticated();

    const table = new AdminRequestsPaginationTable("/admin-requests/status/PENDING");
    common(table);


})