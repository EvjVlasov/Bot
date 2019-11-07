package com.dictionary;

public class Model {
        private String definitions;
        private String examples;
        private String pronunciation;
        private String transcription;

        public String getDefinitions() {
                return definitions;
        }

        public void setDefinitions(String definitions) {
                this.definitions = definitions;
        }

        public String getExamples() {
                return examples;
        }

        public void setExamples(String examples) {
                this.examples = examples;
        }

        public String getPronunciation() {
                return pronunciation;
        }

        public void setPronunciation(String pronunciation) {
                this.pronunciation = pronunciation;
        }

        public String getTranscription() {
                return transcription;
        }

        public void setTranscription(String transcription) {
                this.transcription = transcription;
        }
}
