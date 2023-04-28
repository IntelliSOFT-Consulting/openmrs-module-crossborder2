<%
    ui.includeJavascript("crossborder2", "kenyaemr/controllers/patient.js")

    def heading = config.heading ?: "Matching Patients"
%>
<div class="ke-panel-frame" ng-controller="AdvancedPatientSearchResults" ng-init="init('${ currentApp.id }', '${ config.pageProvider }', '${ config.page }')">
    <div class="ke-panel-heading">${ heading } <input type="checkbox" name="local-results" id="local-results"><label for="local-results">Local Results</label><input type="checkbox" name="remote-results" id="remote-results"><label for="remote-results">Remote Results</label></div>
    <div class="ke-panel-content">
        <div class="ke-stack-item ke-navigable" ng-repeat="patient in results" ng-click="onResultClick(patient)">
            ${ ui.includeFragment("kenyaemr", "patient/result.full") }
        </div>
        <div ng-if="results.length == 0 && !showLoader" style="text-align: center; font-style: italic">None</div>
        <div ng-if= "showLoader" style="text-align: center;">
            <img src="${ ui.resourceLink("kenyaui", "images/loader_small.gif") }" />
        </div>
    </div>
</div>
