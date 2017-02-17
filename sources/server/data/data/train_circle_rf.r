# Do some setup, and train a basic random forest model

library(randomForest)
# data(iris)
dataset = read.csv("dataset/learning.csv")

model <- randomForest(contactType ~ ., data=dataset)

# Make a data frame containing all the tree data

output <- data.frame()

for (i in 1:model[['forest']][['ntree']]) {
  new_values <- getTree(model, i)
  new_values <- cbind(tree = rep(i, nrow(new_values)), new_values)
  
  output <- rbind(output, new_values, make.row.names = FALSE)
}


# A function to look up numeric class levels in the prediction fields

get_class <- function(x) {
  if (x == 0) {
    return(NA)
  } else {
    return(model[['classes']][x])
  }
}

# Replace numeric predictions with original character

output[, 'prediction'] <- do.call(rbind, lapply(output[, 'prediction'], get_class))

# Write output to CSV

write.table(output, 'circleMessenger_forest.csv', sep = ',', row.names = FALSE)