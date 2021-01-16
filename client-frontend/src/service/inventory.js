import resource from "../resource/resource";

export const findAvailableBookCount = async id => {
  console.log("search inventory for", id);
  let response = await resource.get(`/inventory/books/${id}/count`);
  if (response.status !== 200) {
    throw new Error(response.data);
  } else {
    return await response.data;
  }
};
