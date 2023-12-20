package com.poisonedyouth.springcucumber.application.feature

import io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/poisonedyouth/springcucumber/application/feature")
@ConfigurationParameter(
    key = PLUGIN_PROPERTY_NAME,
    value = "html:build/reports/cucumber/result.html"
)
internal class CucumberTest
