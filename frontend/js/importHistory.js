import $ from "jquery";
import {redirectIfAuthenticated} from "./common/utils";
import './common/common';
import './common/modal';
import {PaginationTable} from "./common/pagination";
import {common} from "./common/common";
import '../css/main.css';
import 'simple-notify/dist/simple-notify.css';

class HistoryPaginationTable extends PaginationTable {
    addBodyTable(values) {
        const row = $('<tr>');

        for (const key in values) {
            if (values.hasOwnProperty(key)) {
                const cell = $('<td>').text(values[key]);
                row.append(cell);
            }
        }

        this.table.append(row);
    }
}

$(document).ready(function () {
    redirectIfAuthenticated();
    const tableHistory = new HistoryPaginationTable("/import-history");
    common(tableHistory);
})