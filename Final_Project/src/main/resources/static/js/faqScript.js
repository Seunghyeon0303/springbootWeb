function toggleTab(tabId) {
    // Hide all accordion contents
    var contents = document.getElementsByClassName('accordion-content');
    for (var i = 0; i < contents.length; i++) {
        contents[i].classList.remove('active');
    }

    // Show the selected accordion content
    var selectedContent = document.getElementById(tabId + '-content');
    if (selectedContent) {
        selectedContent.classList.add('active');
    }
}

$(document).ready(function() {
$('.panel-heading').click(function() {
var currentlyOpenAccordion = $(this).parents('.panel-group').find('.in');
var newAccordionToOpen = $(this).next('.panel-collapse');

if (currentlyOpenAccordion && currentlyOpenAccordion != newAccordionToOpen) {
    currentlyOpenAccordion.collapse('hide');
}

newAccordionToOpen.collapse('toggle');
});
});