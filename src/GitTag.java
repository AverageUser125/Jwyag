import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

public class GitTag extends GitCommit {

	public LinkedHashMap<byte[], Object> kvlm;

	public GitTag(byte[] data) {
		super(data);
	}

	@Override
	public String getFmt() {
		return "tag";
	}

	public static void tagCreate(GitRepository repo, String name, String ref, boolean createTagObject)
			throws Exception {
		// Find the object reference
		String sha = GitObjectHelper.objectFind(repo, ref);

		if (createTagObject) {
			// Create a tag object
			GitTag tag = new GitTag(null);
			tag.kvlm.put("object".getBytes(), sha.getBytes());
			tag.kvlm.put("type".getBytes(), "commit".getBytes());
			tag.kvlm.put("tag".getBytes(), name.getBytes());
			tag.kvlm.put("tagger".getBytes(), "Wyag <wyag@example.com>".getBytes());
			tag.kvlm.put(null, "A tag generated by wyag, which won't let you customize the message!".getBytes());

			String tagSha = GitObjectHelper.objectWrite(tag, repo);
			refCreate(repo, "tags/" + name, tagSha);
		} else {
			// Create a lightweight tag (ref)
			refCreate(repo, "tags/" + name, sha);
		}
	}

	private static void refCreate(GitRepository repo, String refName, String sha) throws IOException {
		File refFile = new File(GitObjectHelper.repoFile(repo, "refs/" + refName).toString());

		try (FileWriter writer = new FileWriter(refFile)) {
			writer.write(sha + "\n");
		}
	}
}
