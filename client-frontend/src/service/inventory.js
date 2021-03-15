import resource from "@/resource/resource";
import { sendResponse } from "@/util/responseUtil.js";

export const findAvailableBookCount = async id => {
  let response = await resource.get(`/inventory/books/${id}/count`);
  return sendResponse(response, 200);
};

export const findAllBookReferences = async bookId => {
  let response = await resource.get(`/admin/inventory/books/${bookId}`);
  return sendResponse(response, 200);
};
