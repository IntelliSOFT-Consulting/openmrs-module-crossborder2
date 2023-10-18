<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])

    def menuItems = [
            [ label: "Create new patient", extra: "Patient can't be found", iconProvider: "kenyaui", icon: "buttons/patient_add.png", href: ui.pageLink("crossborder2", "kenyaemr/patient/cbPatientRegistration") ],
            [ label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("kenyaemr", "userHome") ]
    ]
%>

<div class="ke-page-sidebar">
    ${ ui.includeFragment("crossborder2", "kenyaemr/patient/advancedPatientSearch", [ defaultWhich: "all" ]) }

    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>

<div class="ke-page-content">
    ${ ui.includeFragment("crossborder2", "kenyaemr/patient/advancedPatientSearchResults", [ pageProvider: "kenyaemr", page: "registration/registrationViewPatient" ]) }
</div>

