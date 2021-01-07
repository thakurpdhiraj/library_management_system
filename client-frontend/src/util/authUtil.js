export const isAuthenticated = () => {
  return sessionStorage.getItem("user") != null;
};

export const isAdmin = () => {
  let user = JSON.parse(sessionStorage.getItem("user"));
  return user.roles.includes("ADMIN");
};

export const setSessionUser = user => {
  sessionStorage.setItem(
    "user",
    JSON.stringify({
      name: user.username,
      roles: user.userRoles,
      id: user.id
    })
  );
};

export const removeSessionUser = () => {
  sessionStorage.removeItem("user");
};
