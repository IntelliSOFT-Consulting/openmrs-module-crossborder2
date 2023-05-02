<div class="ke-page-sidebar">
    ${ ui.includeFragment("crossborder2", "patient/advancedPatientSearchForm", [ defaultWhich: "all" ]) }

    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>

<div class="ke-page-content">
    ${ ui.includeFragment("crossborder2", "patient/advancedPatientSearchResults", [ pageProvider: "kenyaemr", page: "registration/registrationViewPatient" ]) }
</div>
