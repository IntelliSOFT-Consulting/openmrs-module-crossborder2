<%
    ui.includeJavascript("crossborder2", "kenyaemr/controllers/patient.js")

    def heading = config.heading ?: "Matching Patients"
%>
<div class="ke-panel-frame" ng-controller="AdvancedPatientSearchResults" ng-init="init('${ currentApp }', '${ config.pageProvider }', '${ config.page }')">
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
    <div style="display: none">
        <%= ui.includeFragment("kenyaui", "widget/dialogForm", [
                buttonConfig: [ label: "Check out of visit", iconProvider: "kenyaui", icon: "buttons/visit_end.png" ],
                dialogConfig: [ heading: "Import from Regional CR", width: 50, height: 30 ],
                fields: [
                        [ hiddenInputName: "visitId", value: "" ],
                        [ hiddenInputName: "appId", value: "" ],
                        [ label: "End Date and Time", formFieldName: "stopDatetime", class: java.util.Date, initialValue: new Date(), showTime: true ]
                ],
                fragmentProvider: "kenyaemr",
                fragment: "registrationUtil",
                action: "stopVisit",
                onSuccessCallback: "ui.reloadPage()",
                submitLabel: ui.message("general.submit"),
                cancelLabel: ui.message("general.cancel")
        ]) %>
    </div>

</div>
