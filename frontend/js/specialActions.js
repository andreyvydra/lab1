import $ from "jquery";
import '../css/main.css';
import 'simple-notify/dist/simple-notify.css';
import {getAuthHeader, redirectIfAuthenticated} from "./common/utils";
import {common} from './common/common';
import './common/modal';
import * as c from "./common/constants";
import {errorNotifies} from "./common/error";
import InfoNotify from "./common/notifications/infoNotify";

function renderProducts(list) {
  const body = $('#sa-results-body');
  body.empty();
  const toText = v => (v === null || v === undefined) ? '' : v;
  (list || []).forEach(values => {
    const row = $('<tr>');
    const ordered = ['id','isChangeable','user','name','coordinates','creationDate','manufacturer','unitOfMeasure','price','manufactureCost','rating','owner'];
    ordered.forEach(k => row.append($('<td>').text(toText(values[k]))));
    body.append(row);
  });
}

$(document).ready(function () {
  redirectIfAuthenticated();
  const noopTable = { doRequest: () => {} };
  common(noopTable);

  // delete by owner (returns one product deleted)
  $('#sa-btn-delete-by-owner').on('click', function () {
    const owner = parseInt($('#sa-delete-by-owner').val());
    $.ajax({
      url: `${c.baseUrl}${c.apiUrl}/product/deleteOneByOwner`,
      type: 'DELETE',
      headers: getAuthHeader(),
      data: { owner },
      success: function (response) { new InfoNotify('\u0413\u043e\u0442\u043e\u0432\u043e', '\u0423\u0434\u0430\u043b\u0451\u043d \u043f\u0440\u043e\u0434\u0443\u043a\u0442 ID=' + response.id); },
      error: errorNotifies
    });
  });

  // name starts with
  $('#sa-btn-starts-with').on('click', function () {
    const prefix = $('#sa-starts-with').val();
    $.ajax({
      url: `${c.baseUrl}${c.apiUrl}/product/nameStartsWith`,
      type: 'GET',
      headers: getAuthHeader(),
      data: { prefix },
      success: renderProducts,
      error: errorNotifies
    });
  });

  // rating less
  $('#sa-btn-rating-less').on('click', function () {
    const max = parseInt($('#sa-rating-less').val());
    $.ajax({
      url: `${c.baseUrl}${c.apiUrl}/product/ratingLess`,
      type: 'GET',
      headers: getAuthHeader(),
      data: { max },
      success: renderProducts,
      error: errorNotifies
    });
  });

  // by manufacturer
  $('#sa-btn-by-manufacturer').on('click', function () {
    const manufacturer = parseInt($('#sa-by-manufacturer').val());
    $.ajax({
      url: `${c.baseUrl}${c.apiUrl}/product/byManufacturer`,
      type: 'GET',
      headers: getAuthHeader(),
      data: { manufacturer },
      success: renderProducts,
      error: errorNotifies
    });
  });

  // by unit
  $('#sa-btn-by-unit').on('click', function () {
    const unitOfMeasure = $('#sa-by-unit').val();
    $.ajax({
      url: `${c.baseUrl}${c.apiUrl}/product/byUnit`,
      type: 'GET',
      headers: getAuthHeader(),
      data: { unitOfMeasure },
      success: renderProducts,
      error: errorNotifies
    });
  });
});
