import resource from "@/resource/resource";
import { sendResponse } from "@/util/responseUtil.js";

export const findAllCategories = async () => {
  let response = await resource.get("/categories");
  return sendResponse(response, 200);
};

export const findAllBooks = async () => {
  let response = await resource.get("/books");
  return sendResponse(response, 200);
};

export const saveBook = async book => {
  let response = await resource.post("/admin/books/", book);
  return sendResponse(response, 201);
};
