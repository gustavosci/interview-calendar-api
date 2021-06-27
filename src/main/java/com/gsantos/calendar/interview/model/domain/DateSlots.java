/*
 * Copyright (c) 2021.
 * Author: Gustavo Santos.
 */

package com.gsantos.calendar.interview.model.domain;

import java.time.LocalDate;
import java.util.List;

public class DateSlots {

    private final LocalDate date;
    private final List<Slot> slots;

    public DateSlots(LocalDate date, List<Slot> slots) {
        this.date = date;
        this.slots = slots;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Slot> getSlots() {
        return slots;
    }
}
