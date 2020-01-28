package cse340.sensing.snapshot;

import android.content.res.Resources;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.awareness.state.Weather;

import java.util.Arrays;

import cse340.sensing.ContextActivity;
import cse340.sensing.R;

public class WeatherSnapshotListener implements ContextActivity.SnapshotListener<WeatherResponse> {
    private final TextView mUpdate;

    private final Resources mResources;

    private final ImageView imageView;

    public WeatherSnapshotListener(TextView update, Resources resources, ImageView imageView) {
        mUpdate = update;
        mResources = resources;
        this.imageView = imageView;
    }

    @Override
    public void onSnapshot(WeatherResponse response) {


        Weather weather = response.getWeather();
        StringBuilder weatherInfo = new StringBuilder();
        StringBuilder conditionInfo = new StringBuilder();
        Arrays.stream(weather.getConditions()).forEach((w) -> conditionInfo.append(retrieveConditionString(w)).append(", "));

        // Deleting the trailing space and comma
        conditionInfo.deleteCharAt(conditionInfo.length() - 1);
        conditionInfo.deleteCharAt(conditionInfo.length() - 1);

        // Setting the image based on the current weather
        if (conditionInfo.toString().contains("Sunny")) {
            imageView.setImageResource(R.mipmap.sunny);
        } else if (conditionInfo.toString().contains("Rainy")) {
            imageView.setImageResource(R.mipmap.rainy);
        } else if (conditionInfo.toString().contains("Cloudy")) {
            imageView.setImageResource(R.mipmap.cloudy);
        }

        weatherInfo.append(mResources.getString(R.string.weather_cond_format, conditionInfo)).append("\n");
        weatherInfo.append(mResources.getString(R.string.weather_humid_format, weather.getHumidity())).append("\n");
        weatherInfo.append(mResources.getString(R.string.weather_temp_format, weather.getTemperature(weather.CELSIUS))).append("\n");
        weatherInfo.append(mResources.getString(R.string.weather_dew_format, weather.getDewPoint(weather.CELSIUS))).append("\n");
        weatherInfo.append(mResources.getString(R.string.weather_feels_format, weather.getFeelsLikeTemperature(weather.CELSIUS))).append("\n");

        // Finally outputting the text
        mUpdate.setText(weatherInfo);

    }


    /**
     * Translates an weather constant to a string representation. Do not modify.
     */
    private String retrieveConditionString(int condition) {
        switch (condition) {
            case Weather.CONDITION_CLEAR:
                return "Clear";
            case Weather.CONDITION_CLOUDY:
                return "Cloudy";
            case Weather.CONDITION_FOGGY:
                return "Foggy";
            case Weather.CONDITION_HAZY:
                return "Hazy";
            case Weather.CONDITION_ICY:
                return "Icy";
            case Weather.CONDITION_RAINY:
                return "Rainy";
            case Weather.CONDITION_SNOWY:
                return "Snowy";
            case Weather.CONDITION_STORMY:
                return "Stormy";
            case Weather.CONDITION_WINDY:
                return "Windy";
            default:
                return "Unknown";
        }
    }
}