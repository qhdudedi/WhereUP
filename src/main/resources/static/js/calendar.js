document.addEventListener('DOMContentLoaded', function() {
    /** 날짜 한국으로 변경*/
        // function formatDateToKst(date) {
        //   var kstDate = new Date(date.getTime() + (9 * 60 * 60 * 1000));
        //   return kstDate.toISOString().slice(0, 10);
        // }
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView : 'dayGridMonth'
        , timeZone: 'Asia/Seoul'
        , headerToolbar: {
            right: 'today prev,next'
        }
        , dayMaxEventRows: 4
        , droppable: false
        , height: $(window).height() * 0.83
        , contentHeight: $(window).height() * 0.83
        , aspectRatio: 2
        , locale : 'ko'
        , selectable: true
        , events: function(fetchInfo, successCallback, failureCallback) {
            // 서버에서 이벤트 데이터를 가져옵니다.
            $.ajax({
                url: '/calendar/events', // 서버 API 엔드포인트
                method: 'GET',
                success: function(data) {
                    var events = data.map(function(event) {
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
        }
        , select: function (info) {
            $('#modalTitle').text('Create Event');
            $('#eventId').val('');
            $('#title').val('');
            $('#summary').val('');
            // $('#startDate').val(formatDateToKst(new Date(info.start)));
            // $('#endDate').val(formatDateToKst(new Date(info.end)));
            $('#startDate').val(info.startStr);
            $('#endDate').val(info.endStr);
            $('#eventModal').show();
        }
        , editable: true
        , eventClick: function (info) {
            var event = info.event;
            $('#modalTitle').text('Edit Event');
            $('#eventId').val(event.id);
            $('#title').val(event.title);
            $('#summary').val(event.extendedProps.summary);
            // $('#startDate').val(formatDateToKst(new Date(event.start)));
            // $('#endDate').val(formatDateToKst(new Date(event.end)));
            var startDate = new Date(event.start).toISOString().slice(0, 10);
            var endDate = event.end ? new Date(event.end).toISOString().slice(0, 10) : '';
            $('#startDate').val(startDate);
            $('#endDate').val(endDate);
            $('#deleteEventBtn').show();
            $('#eventModal').show();
        }
    });
    calendar.render();

  // var initialEventCount = calendar.getEvents().length;
    // 모달 닫기 기능
    $('.close').on('click', function () {
        $('#eventModal').hide();
        $('#deleteEventBtn').hide()
    });
    // 모달 폼 제출
    $('#eventForm').on('submit', function (e) {
        e.preventDefault();
        var eventId = $('#eventId').val();
        var title = $('#title').val();
        var summary = $('#summary').val();
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();

        var eventData = {
            title: title,
            summary: summary,
            startDate: startDate,
            endDate: endDate
        };

        let allEvents = calendar.getEventSources();
        console.log(allEvents);

        if (eventId) {
            // 일정 수정
            var event = calendar.getEventById(eventId);
            event.setProp('title', title);
            event.setExtendedProp('summary', summary);
            event.setStart(startDate);
            event.setEnd(endDate);

            $.ajax({
                url: '/calendar/edit/' + eventId,
                method: 'PATCH',
                contentType: 'application/json',
                data: JSON.stringify(eventData),
                success: function (response) {
                    eventData.id = response.id;
                    calendar.refetchEvents();
                    $('#eventModal').hide();
                    $('#deleteEventBtn').hide();
                },
                error: function () {
                    alert('Failed to update event.');
                }
            });
        } else {
            // 일정 등록
            $.ajax({
                url: '/calendar',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(eventData),
                success: function (response) {
                    eventData.id = response.id; // 서버가 반환한 ID를 사용
                    calendar.addEvent(eventData);
                    $('#eventModal').hide();
                    $('#deleteEventBtn').hide();
                    console.log(eventData)
                },
                error: function () {
                    alert('Failed to create event.');
                }
            });
        }
    });
    $('#deleteEventBtn').on('click', function() {
        var eventId = $('#eventId').val();
        if (eventId) {
            // if(confirm('일정을 삭제하시겠습니까?'))
            // 일정 삭제
            $.ajax({
                url: '/calendar/' + eventId,
                method: 'DELETE',
                success: function(response) {
                    var event = calendar.getEventById(eventId);
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