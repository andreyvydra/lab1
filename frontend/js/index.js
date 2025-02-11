import '../css/main.css';
import 'simple-notify/dist/simple-notify.css';
import $ from "jquery";
import {redirectIfAuthenticated} from "./common/utils";

$(document).ready(function() {
    redirectIfAuthenticated();
})
