package com.File.Pricing.Strategy.System.ml;

import org.springframework.stereotype.Component;
import weka.classifiers.functions.LinearRegression;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.InputStream;

@Component
public class WekaPricingModel {


    private LinearRegression model;
    private Instances dataset;

    public void trainModel() {
        // Load training data from the classpath (src/main/resources).
        // This avoids relying on a working-directory relative path at runtime.
        try (InputStream in = WekaPricingModel.class.getClassLoader().getResourceAsStream("pricing-data.arff")) {
            if (in == null) {
                throw new IllegalStateException(
                        "Training dataset 'pricing-data.arff' not found on the classpath. " +
                                "Expected it under src/main/resources/pricing-data.arff."
                );
            }

            ArffLoader loader = new ArffLoader();
            loader.setSource(in);
            Instances data = loader.getDataSet();
            if (data == null || data.numAttributes() == 0) {
                throw new IllegalStateException("Training dataset is empty or unreadable (pricing-data.arff).");
            }

            // Target (last column = price)
            data.setClassIndex(data.numAttributes() - 1);

            LinearRegression lr = new LinearRegression();
            lr.buildClassifier(data);

            this.dataset = data;
            this.model = lr;

            System.out.println("Weka model trained. Dataset rows: " + data.numInstances());
        } catch (Exception e) {
            // Fail fast with a clear reason so the app doesn't start in a broken state.
            throw new IllegalStateException("Failed to train Weka model.", e);
        }
    }

    public double predict(double demand, double competitorPrice, double stock) {
        try {
            if (dataset == null || model == null) {
                throw new IllegalStateException("Model is not trained yet. Call trainModel() before predict().");
            }

            DenseInstance instance = new DenseInstance(dataset.numAttributes());
            instance.setDataset(dataset);

            instance.setValue(0, demand);
            instance.setValue(1, competitorPrice);
            instance.setValue(2, stock);

            return model.classifyInstance(instance);

        } catch (Exception e) {
            throw new RuntimeException("Prediction failed", e);
        }

    }



}
