package com.wilsonsinclair.scheduler;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

/*
    This class is used to store settings for the application
    It is purely a utility class
    All of this class's data is written to settings.json

    On launch of the application, the settings are read from settings.json and stored in-memory.
    Every other runtime read of settings.json will read from memory until the application is closed.

    @author Wilson Sinclair

 */
public final class Settings {

    private static Settings INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(Settings.class);

    private int numLunchers;
    private int numClosers;
    private int managerHours;

    private static final File settingsFile = new File("settings.json");

    private Settings(int numLunchers, int numClosers, int managerHours) {
        this.numLunchers = numLunchers;
        this.numClosers = numClosers;
        this.managerHours = managerHours;
    }

    private Settings() {
        this(3, 2, 50);
    }

    public static synchronized Settings getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = readSettings();
            } catch (IOException e) {
                logger.error("Failed to read settings, using defaults.", e);
                INSTANCE = new Settings();
                try {
                    INSTANCE.save();
                } catch (IOException ioException) {
                    logger.error("Failed to save default settings.", ioException);
                }
            }
        }
        return INSTANCE;
    }

    public int getNumLunchers() {
        return numLunchers;
    }

    public void setNumLunchers(int numLunchers) {
        this.numLunchers = numLunchers;
    }

    public int getNumClosers() {
        return numClosers;
    }

    public void setNumClosers(int numClosers) {
        this.numClosers = numClosers;
    }

    public int getManagerHours() {
        return managerHours;
    }

    public void setManagerHours(int managerHours) {
        this.managerHours = managerHours;
    }

    public void save() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(settingsFile, false), StandardCharsets.UTF_8)) {
            writer.write(gson.toJson(this));
            logger.info("Settings saved to {}", settingsFile.getAbsolutePath());
        }
    }

    private static Settings readSettings() throws IOException {
        if (!settingsFile.exists()) {
            logger.info("Settings file not found. Creating with default values.");
            Settings defaultSettings = new Settings();
            defaultSettings.save();
            return defaultSettings;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Reader reader = new FileReader(settingsFile)) {
            logger.info("Reading settings from {}", settingsFile.getAbsolutePath());
            return gson.fromJson(reader, Settings.class);
        }
    }

    @Override
    public String toString() {
        return "Settings{" +
                "numLunchers=" + numLunchers +
                ", numClosers=" + numClosers +
                ", managerHours=" + managerHours +
                '}';
    }
}

