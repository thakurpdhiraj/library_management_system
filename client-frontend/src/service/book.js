import resource from "../resource/resource";

export const findAllCategories = async () => {
  let response = await resource.get("/categories");
  if (!response.status === 200) {
    throw new Error(response.data);
  } else {
    return await response.data;
  }
};

export const findAllBooks = async () => {
  let response = await resource.get("/books");
  if (!response.status === 200) {
    throw new Error(response.data);
  } else {
    return await response.data;
  }
};
