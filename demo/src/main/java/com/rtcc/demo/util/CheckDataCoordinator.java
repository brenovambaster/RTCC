package com.rtcc.demo.util;

import com.rtcc.demo.DTOs.CoordinatorRequestDTO;

public class CheckDataCoordinator {
    /**
     * Check if the data is valid for creating a coordinator
     *
     * @param data
     */
    public static void checkDataCreate(CoordinatorRequestDTO data) {
        if (data == null || data.course() == null || data.password() == null || data.name() == null || data.email() == null) {
            throw new IllegalArgumentException("Invalid data provided");
        }
    }

    /**
     * Check if the data is valid for updating a coordinator
     *
     * @param data
     */
    public static void checkDataUpdate(CoordinatorRequestDTO data) {
        if (data == null || data.course() == null || data.name() == null || data.email() == null) {
            throw new IllegalArgumentException("Invalid data provided");
        }
    }
}
