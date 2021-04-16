import resource from "@/resource/resource";
import { sendResponse } from "@/util/responseUtil.js";
import { omitBy, isEmpty } from "lodash";

export const findAllCategories = async () => {
  let response = await resource.get("/categories");
  return sendResponse(response, 200);
};

export const findAllBooks = async book => {
  const params = new URLSearchParams(omitBy(book, isEmpty));
  let response = await resource.get("/books", { params: params });
  return sendResponse(response, 200);
};

export const findBook = async id => {
  let response = await resource.get(`/books/${id}`);
  return sendResponse(response, 200);
};

export const saveBook = async book => {
  let response = await resource.post("/admin/books/", book);
  return sendResponse(response, 201);
};

export const updateBook = async book => {
  let response = await resource.put(`/admin/books/${book.id}`, book);
  return sendResponse(response, 200);
};

export const deleteBook = async (id, references) => {
  const params = new URLSearchParams(references.map(s => ["bookReference", s]));
  let response = await resource.delete(`/admin/books/${id}`, {
    params: params
  });
  return sendResponse(response, 204);
};
