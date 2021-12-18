package swagger.automate.swagger.bean;

public class ResponseAndCode {
	private int producesBodyKey;
	private boolean array;
	private int responseCode;

	public int getProducesBodyKey() {
		return producesBodyKey;
	}

	public void setProducesBodyKey(int producesBodyKey) {
		this.producesBodyKey = producesBodyKey;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public boolean isArray() {
		return array;
	}

	public void setArray(boolean array) {
		this.array = array;
	}

}
