import resource from "@/resource/resource";
import { sendResponse } from "@/util/responseUtil.js";

export const findAvailableBookCount = async id => {
  console.log("search inventory for", id);
  let response = await resource.get(`/inventory/books/${id}/count`);
  return sendResponse(response, 200);
};
