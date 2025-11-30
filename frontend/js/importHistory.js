import $ from "jquery";
import {getAuthHeader, redirectIfAuthenticated} from "./common/utils";
import ErrorNotify from "./common/notifications/errorNotify";
import './common/common';
import './common/modal';
import {PaginationTable} from "./common/pagination";
import {common} from "./common/common";
import '../css/main.css';
import 'simple-notify/dist/simple-notify.css';

class HistoryPaginationTable extends PaginationTable {
    addBodyTable(values) {
        const row = $('<tr>');
        row.append($('<td>').text(values['id']));
        row.append($('<td>').text(values['user']));
        row.append($('<td>').text(values['startTime']));
        row.append($('<td>').text(values['endTime']));
        row.append($('<td>').text(values['status']));
        row.append($('<td>').text(values['addedObjects']));
        row.append($('<td>').text(values['originalFileName'] || '-'));

        const downloadCell = $('<td>');
        if (values['downloadUrl']) {
            const link = $('<a>')
                .attr('href', '#')
                .text('Скачать')
                .on('click', async (e) => {
                    e.preventDefault();
                    try {
                        const response = await fetch(values['downloadUrl'], { headers: getAuthHeader() });
                        if (!response.ok) {
                            let message = 'Сервис для работы с файлами недоступен';
                            try {
                                const data = await response.json();
                                if (data?.message) message = data.message;
                            } catch (_) { /* ignore parse errors */ }
                            throw new Error(message);
                        }
                        const blob = await response.blob();
                        const objectUrl = URL.createObjectURL(blob);
                        const a = document.createElement('a');
                        a.href = objectUrl;
                        a.download = values['originalFileName'] || 'import-file';
                        document.body.appendChild(a);
                        a.click();
                        a.remove();
                        URL.revokeObjectURL(objectUrl);
                    } catch (err) {
                        console.error('File download failed', err);
                        new ErrorNotify('Не удалось загрузить файл', err.message || 'Сервис для работы с файлами недоступен');
                    }
                });
            downloadCell.append(link);
        } else {
            downloadCell.text('-');
        }
        row.append(downloadCell);
        this.table.append(row);
    }
}

$(document).ready(function () {
    redirectIfAuthenticated();
    const tableHistory = new HistoryPaginationTable("/import-history");
    common(tableHistory);
})
