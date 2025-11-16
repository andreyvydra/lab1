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
        const orderedKeys = ['id', 'user', 'startTime', 'endTime', 'status', 'addedObjects'];
        orderedKeys.forEach(k => row.append($('<td>').text(values[k])));
        this.table.append(row);
    }
}

$(document).ready(function () {
    redirectIfAuthenticated();
    const tableHistory = new HistoryPaginationTable("/import-history");
    common(tableHistory);
})
