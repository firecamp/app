package ca.xef6.app.db;

final class TableColumn {

    public final String name;
    public final String type;

    public TableColumn(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        throw new NullPointerException("Fuck you! Don't call toString on a TableColumn.");
    }

}
