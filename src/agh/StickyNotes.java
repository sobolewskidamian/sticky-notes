package agh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class StickyNotes extends Abstract {
    private String path;
    private LinkedList<Note> notes = new LinkedList<>();

    private Note create(String text, String fileName) throws FileNotFoundException {
        Note note = new Note(text, this.path, fileName, this.notes);
        notes.add(note);
        return note;
    }

    public void delete(Note note) {
        notes.remove(note);
    }

    private void loadNotes() throws IOException {
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(this.path), "*.{sn}");
        for (Path it : stream) {
            String linesStr = "";
            List<String> lines = Files.readAllLines(it.toAbsolutePath(), StandardCharsets.UTF_8);
            for (String line : lines)
                linesStr += line + "\n";
            create(linesStr, it.getFileName().toString());
        }
    }

    public void run() throws IOException {
        Path p = Paths.get("StickyNotesData");
        if(!Files.exists(p)){
            new File(p.toString()).mkdir();
        }
        this.path = p.toAbsolutePath().toString()+"\\";
        loadNotes();
        if (this.notes.isEmpty()) {
            String fileName = "1.sn";
            String actPath = this.path + fileName;
            new File(actPath).createNewFile();
            settings(0, 4, this.path, fileName);
            create("", fileName);
        }
    }
}
