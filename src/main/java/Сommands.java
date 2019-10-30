enum Commands {

    PRONUNCIATION("/pronunciations"),
    DEFINITION("/definitions"),
    EXAMPLES( "/examples");

    
    private String description;

    Commands(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }



}
