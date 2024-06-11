package com.project.whereup.calendar.entity;

import com.project.whereup.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calendar {
        @Id
        @Column(name="event_id")
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        private Long id;

        @Column(name="title")
        private String title;

        @Column(name="startDate")
        private LocalDate startDate;

        @Column(name="endDate")
        private LocalDate endDate;

        @Column(name="summary")
        private String summary;

        /** LAZY->EAGER 변경으로 noSession 문제 해결 ? */
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        private User user;

        public void edit(String title, String summary, LocalDate startDate, LocalDate endDate){
                this.title = title;
                this.summary = summary;
                this.startDate = startDate;
                this.endDate = endDate;
        }

}
