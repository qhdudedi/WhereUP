document.addEventListener('DOMContentLoaded', function() {
    let calendarEl = document.getElementById('calendar');
    let calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        timeZone: 'Asia/Seoul',
        headerToolbar: {
            right: 'today prev,next'
        },
        dayMaxEventRows: 4,
        droppable: false,
        height: $(window).height() * 0.83,
        contentHeight: $(window).height() * 0.83,
        aspectRatio: 2,
        locale: 'ko',
        selectable: true,
        eventContent: function(arg) {
            return { html: '<div>' + arg.event.title + '</div>' };
        },
        events: function(fetchInfo, successCallback, failureCallback) {
            $.ajax({
                url: '/calendar/events',
                method: 'GET',
                success: function(data) {
                    let events = data.map(function(event) {
                        return {
                            id: event.id,
                            title: event.title,
                            summary: event.summary,
                            start: event.startDate,
                            end: event.endDate,
                        };
                    });
                    successCallback(events);
                },
                error: function() {
                    failureCallback("FAIL");
                }
            });
        },
        eventContent: function(arg) {
            let titleElement = document.createElement('div');
            titleElement.innerHTML = arg.event.title;
            return { domNodes: [titleElement] };
        },
        select: function(info) {
            $('#modalTitle').text('Create Event');
            $('#eventId').val('');
            $('#title').val('');
            $('#summary').val('');
            $('#startDate').val(info.startStr);
            $('#endDate').val(info.endStr);
            $('#eventModal').show();
        },
        editable: true
        ,eventClick: function(info) {
            let event = info.event;
            $('#modalTitle').text('Edit Event');
            $('#eventId').val(event.id);
            $('#title').val(event.title);
            $('#summary').val(event.extendedProps.summary);
            $('#startDate').val(new Date(event.start).toISOString().slice(0, 10));
            $('#endDate').val(event.end ? new Date(event.end).toISOString().slice(0, 10) : '');
            $('#deleteEventBtn').show();
            $('#eventModal').show();
        }
    });
    calendar.render();

    $('.close').on('click', function() {
        $('#eventModal').hide();
        $('#deleteEventBtn').hide();
    });

    $('#eventForm').on('submit', function(e) {
        e.preventDefault();
        let eventId = $('#eventId').val();
        let title = $('#title').val();
        let summary = $('#summary').val();
        let startDate = $('#startDate').val();
        let endDate = $('#endDate').val();

        // 기본 시간 값을 설정
        let startTime = '08:00:00'; // 기본 시작 시간
        let endTime = '23:59:59'; // 기본 종료 시간

        // 날짜와 시간을 결합하여 ISO 형식으로 변환
        let startDateTime = startDate + 'T' + startTime;
        let endDateTime = endDate + 'T' + endTime;

        let eventData = {
            title: title,
            summary: summary,
            startDate: startDateTime,
            endDate: endDateTime
        };

        if (eventId) {
            let event = calendar.getEventById(eventId);
            event.setProp('title', title);
            event.setExtendedProp('summary', summary);
            event.setStart(startDateTime);
            event.setEnd(endDateTime);

            $.ajax({
                url: '/calendar/edit/' + eventId,
                method: 'PATCH',
                contentType: 'application/json',
                data: JSON.stringify(eventData),
                success: function(response) {
                    eventData.id = response.id;
                    calendar.refetchEvents();
                    $('#eventModal').hide();
                    $('#deleteEventBtn').hide();
                }
            });
        } else {
            $.ajax({
                url: '/calendar',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(eventData),
                success: function(response) {
                    eventData.id = response.id;
                    calendar.addEvent(eventData);
                    calendar.refetchEvents();
                    $('#eventModal').hide();
                    $('#deleteEventBtn').hide();
                },
                error: function() {
                    alert('Failed to create event.');
                }
            });
        }
    });

    $('#deleteEventBtn').on('click', function() {
        let eventId = $('#eventId').val();
        if (eventId) {
            $.ajax({
                url: '/calendar/' + eventId,
                method: 'DELETE',
                success: function(response) {
                    let event = calendar.getEventById(eventId);
                    event.remove();
                    $('#eventModal').hide();
                    $('#deleteEventBtn').hide();
                },
                error: function() {
                    alert('Failed to delete event.');
                }
            });
        }
    });
});
